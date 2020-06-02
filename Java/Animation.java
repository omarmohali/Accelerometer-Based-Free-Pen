import javafx.geometry.Point2D;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.fazecast.jSerialComm.SerialPort;



public class Animation implements ActionListener{
	
	static DecimalFormat df = new DecimalFormat();
	
	static SerialPort port;
	static BufferedReader br;
	static String input = "";
	
	static LinkedList<Double> xAvgQueue = new LinkedList<Double>();
	static LinkedList<Double> yAvgQueue = new LinkedList<Double>();
	static LinkedList<Double> zAvgQueue = new LinkedList<Double>();
	
	static double sumX = 0;
	static double sumY = 0;
	static double sumZ = 0;
	
	static double X_OFFSET = 0;
	static double Y_OFFSET = 0;
	static double Z_OFFSET = 0;
	
	static int QUEUE_SIZE = 1;
	static int sample = 0;
	
	static int SAMPLES_TO_STABLE = 2000;
	static int SAMPLES_TO_CALIBRATE = SAMPLES_TO_STABLE + 500;
	static int CALIBRATION_SAMPLES = SAMPLES_TO_CALIBRATE - SAMPLES_TO_STABLE;
	static int SAMPLES_TO_START = SAMPLES_TO_CALIBRATE;
	
	static float POS_ERROR = 20;
	static float NEG_ERROR = -1 * POS_ERROR;
	
	static boolean writing = false;
	
	static double DELAY = 0.010;
	
	static double INIT_X = 650;
	static double INIT_Y = 300;
	
	static double xDisp = 0;
	static double yDisp = 0;
	
	double x = INIT_X;
	double y = INIT_Y;

	static ArrayList<Point2D> points = new ArrayList<Point2D>();
	
	public static void main(String[] args) throws IOException {
		
		df.setMaximumFractionDigits(2);
		
		port = SerialPort.getCommPorts()[2];
		port.openPort();
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		System.out.println(port.getBaudRate());
		port.setBaudRate(115200);
		br = new BufferedReader(new InputStreamReader(port.getInputStream()));
		
		System.out.println("WAITING FOR AVG QUEUE TO BE FULL.....");
		int c = 0;
		while(true && xAvgQueue.size() < QUEUE_SIZE)
		{
			if(br.ready())
			{
				
				double xVal;
				double yVal;
				double zVal;
				String[] input = br.readLine().split("\t");
				
				try
				{
					xVal = Double.parseDouble(input[0]);
					yVal = Double.parseDouble(input[1]);
					zVal = Double.parseDouble(input[2]);
				}
				catch(NumberFormatException|ArrayIndexOutOfBoundsException e)
				{
					System.out.println("exception :(");
					continue;
				}
				
				System.out.println(c++);
				
				sumX += xVal;
				sumY += yVal;
				sumZ += zVal;
				
				xAvgQueue.addLast(xVal);
				yAvgQueue.addLast(yVal);
				zAvgQueue.addLast(zVal);
			}

		}
		
		System.out.println("AVG QUEUE IS FULL!!!");
		
		Animation gui = new Animation();
		gui.play();
	}

	public void play() throws IOException {
		
		JFrame frame = null;
		JFrame bFrame = null;
		Button button = new Button("Reset");
		button.addActionListener(this);
		
		DrawPanel draw = null;
		
		int sampleCount = 0;
		double xSampleSum = 0.0;
		double ySampleSum = 0.0;
		double zSampleSum = 0.0;
		
		boolean flag = true;
		boolean started = false;
		
		double xVel = 0;
		double yVel = 0;
		
		
		
		while(true) {
			
			String[] input = br.readLine().split("\t");
			
			if(sampleCount > SAMPLES_TO_START && !started)
			{
				bFrame = new JFrame();
				bFrame.getContentPane().add(button);
				bFrame.setSize(50,  50);
				bFrame.setLocation(500, 700);
				bFrame.setVisible(true);
				
				frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				draw = new DrawPanel();
				frame.getContentPane().add(draw);
				frame.setSize(1300, 600);
				frame.setVisible(true);
				started = true;
			}
			
			double xAcc = sumX / QUEUE_SIZE - X_OFFSET;
			double yAcc = sumY / QUEUE_SIZE - Y_OFFSET;
			double zAcc = sumZ / QUEUE_SIZE - Z_OFFSET;
			
			
			double xSample;
			double ySample;
			double zSample;
			
			try
			{
				xSample = Double.parseDouble(input[0]);
				ySample = Double.parseDouble(input[1]);
				zSample = Double.parseDouble(input[2]);
				
				if(input[3].equals("0"))
					writing = false;
				else writing = true;
				
				if(xSample > 1000 || xSample < -1000)
					continue;
				
				
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			

			
			if(xAcc > NEG_ERROR && xAcc < POS_ERROR)
				xAcc = 0;
			
			if(yAcc > NEG_ERROR && yAcc < POS_ERROR)
				yAcc = 0;
			
			if(zAcc > NEG_ERROR && zAcc < POS_ERROR)
				zAcc = 0;


			
			sumX = sumX - xAvgQueue.removeFirst() + xSample;
			xAvgQueue.addLast(xSample);
			
			sumY = sumY - yAvgQueue.removeFirst() + ySample;
			yAvgQueue.addLast(ySample);
			
			sumZ = sumZ - zAvgQueue.removeFirst() + zSample;
			zAvgQueue.addLast(zSample);
			
			if(sampleCount > SAMPLES_TO_STABLE && sampleCount <= SAMPLES_TO_CALIBRATE)
			{
				xSampleSum += xAcc;
				ySampleSum += yAcc;
				zSampleSum += zAcc;
				sampleCount++;
			}
			else if(sampleCount > SAMPLES_TO_CALIBRATE && flag)
			{
				X_OFFSET = xSampleSum / CALIBRATION_SAMPLES;
				Y_OFFSET = ySampleSum / CALIBRATION_SAMPLES;
				Z_OFFSET = zSampleSum / CALIBRATION_SAMPLES;
				flag = !flag;
				sampleCount++;
			}
			else if(sampleCount > SAMPLES_TO_START)
			{
				
				xVel += xAcc * DELAY;
				yVel += yAcc * DELAY;
				
				if(xAcc == 0)
					xVel = 0;
				if(yAcc == 0)
					yVel = 0;
				
				xDisp += xVel * DELAY;
				yDisp += yVel * DELAY;
				
				if(x < 0)
					x = 0;
				if(y < 0)
					y = 0;
				if(x > 1275)
					x = 1275;
				if(y > 573)
					y = 573;
				
				x = INIT_X + xDisp * 10;
				y = INIT_Y - yDisp * 10;
				
				
				

				
				
				
				
				if(writing)
					points.add(new Point2D(x, y));
				
				draw.repaint();  // tells the panel to redraw itself so we can see the circle in new location
			}
			else
			{
				sampleCount++;
			}
				
			
			
				System.out.print("Acc:\t" + df.format(xAcc) + "\t" + df.format(yAcc) + "\t" + df.format(zAcc) + "\t\t");
				System.out.print("Vel:\t" + df.format(xVel) + "\t" + df.format(yVel) + "\t\t");
				System.out.print("Disp:\t" + df.format(xDisp) + "\t" + df.format(yDisp) + "\t\t");
				System.out.println(sampleCount);

		}
	}

	class DrawPanel extends JPanel {
		
		public void paintComponent(Graphics g) {
			try{
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(Color.BLACK);
				
				g.fillOval((int)x, (int)y, 5, 5);
				
				for(Point2D point : points)
					g.fillOval((int)point.getX(), (int)point.getY(), 5, 5);
			}
			catch(Exception e)
			{
				return;
			}
			
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Reset"))
		{
			points.clear();
			xDisp = 0;
			yDisp = 0;
		}
		
	}
}
