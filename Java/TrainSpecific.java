import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;



public class TrainSpecific {
	
	static DecimalFormat df = new DecimalFormat();
	
	
	static ArrayList<Double>  xValuesAL = new ArrayList<Double>();
	static ArrayList<Double>  yValuesAL = new ArrayList<Double>();
	
	static double[] xValuesA = new double[64];
	static double[] yValuesA = new double[64];
	
	static boolean writing = false;
	
	public static void main(String[] args) throws IOException {
			
		Scanner sc = new Scanner(System.in);
		
		
		
		df.setMaximumFractionDigits(2);
		
		
			SerialPort port = SerialPort.getCommPorts()[2];
			System.out.println(port.getSystemPortName());
			port.setBaudRate(115200);
			System.out.println(port.openPort());
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
			
			FileWriter fw = new FileWriter(new File("/Users/omarali/Documents/Bachelor/DataSets/DotsAndBack.txt"), true);
			
			
			
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(port.getInputStream()));
			String line = "";
			
			double xOffset = 0;
			double yOffset = 0;
			
			for(int i = 0; i < 2500;)
			{
				String input  = br.readLine(); 
				
				if(br.ready())
				{
					if(i < 2000)
					{
						System.out.println(input + "\t\t\t" + i);
						i++;
					}
					else
					{
						String[] values = input.split("\t");
						try{
							xOffset += Double.parseDouble(values[0]);
							yOffset += Double.parseDouble(values[1]);
							System.out.println(input + "\t\t\t" + i);
							i++;
						}
						catch(NumberFormatException e)
						{
							continue;
						}
					}
					
					
				}
				
				
					
			}
			
			xOffset /= 500.0;
			yOffset /= 500.0;
			
			System.out.println("Calibrated");
			System.out.println("Please enter the character you would like to train:");
			char in = '.';
			int label = (int)in;
			
			int count = 0;
			
			while(true)
			{
				String[] values;
				
				if(br.ready())
				{
					line = br.readLine();

					values = line.split("\t");
					try
					{
						writing = values[3].equals("1") ? true: false;
					}
					catch(IndexOutOfBoundsException e)
					{
						continue;
					}
					
				}

				if(writing)
				{
				
					while(writing)
					{
						if(br.ready())
						{
							line = br.readLine();
							values = line.split("\t");

							try
							{
								double x = Double.parseDouble(values[0]) - xOffset > -15 && Double.parseDouble(values[0]) - xOffset < 15 ? 0: Double.parseDouble(values[0]) - xOffset;
								double y = Double.parseDouble(values[1]) - yOffset > -15 && Double.parseDouble(values[1]) - yOffset < 15 ? 0: Double.parseDouble(values[1]) - yOffset;
								
								xValuesAL.add(x);
								yValuesAL.add(y);
								
								
								
								writing = values[3].equals("1") ? true: false;
							}
							catch(ArrayIndexOutOfBoundsException e)
							{
								continue;
							}
						}
					}
					
					// resizing the array
					xValuesA = ResizeArray.resize(xValuesAL, 64);
					yValuesA = ResizeArray.resize(yValuesAL, 64);
					
					
					 
					
					fw.write(label + " ");
					
					for(int k = 0; k < 64; k++)
					{	
						
						if(xValuesA[k] > 0)
						{
							fw.write(k + ":" + "1"+ " ");
						}
						else if(xValuesA[k] < 0)
						{
							fw.write(k + ":" + "-1"+ " ");
						}
						else
						{
							fw.write(k + ":" + "0"+ " ");
						}
					}
	
					for(int k = 64; k < 128; k++)
					{
						if(k == 127)
						{

							if(yValuesA[63] > 0)
							{
								fw.write(k + ":" + "1"+ "\n");
							}
							else if(yValuesA[63] < 0)
							{
								fw.write(k + ":" + "-1"+ "\n");
							}
							else
							{
								fw.write(k + ":" + "0"+ "\n");
							}
						}
						else
						{
							if(yValuesA[k - 64] > 0)
							{
								fw.write(k + ":" + "1"+ " ");
							}
							else if(yValuesA[k - 64] < 0)
							{
								fw.write(k + ":" + "-1"+ " ");
							}
							else
							{
								fw.write(k + ":" + "0"+ " ");
							}
							
						}
						
					}
					
					System.out.println("done!");
					fw.flush();
					
					xValuesAL.clear();
					yValuesAL.clear();
					System.out.println(++count);
					
				}
				
				
				
				
			}
			
			
			
			
			
			
		}

}
