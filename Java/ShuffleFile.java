import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class ShuffleFile {
	
	public static void main(String[] args) throws IOException {
		
		randomize("/Users/omarali/Documents/Bachelor/DataSets/DataSetCopy.txt", "/Users/omarali/Documents/Bachelor/DataSets/DataSetShuffled.txt");
		divideSet();
		System.out.println("Done!");
		
	}
	
	public static void randomize(String file1, String file2) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file1));
		ArrayList<String> a = new ArrayList<String>();
		
		while(br.ready())
		{
			a.add(br.readLine());
		}
		
		System.out.println(a.size());
		
		FileWriter fw = new FileWriter(new File(file2), false);
		
		while(!a.isEmpty())
		{
			int randomNo = (int) (Math.random() * a.size());
			fw.write(a.remove(randomNo) + "\n");
		}
		
		fw.flush();
		fw.close();
		
		System.out.println("Shuffled");
	}

	public static void randomize() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/omarali/Documents/Bachelor/DataSets/DataSet1.txt"));
		ArrayList<String> a = new ArrayList<String>();
		
		while(br.ready())
		{
			a.add(br.readLine());
		}
		
		System.out.println(a.size());
		
		FileWriter fw = new FileWriter(new File("/Users/omarali/Documents/Bachelor/DataSets/DataSetShuffled.txt"), false);
		
		while(!a.isEmpty())
		{
			int randomNo = (int) (Math.random() * a.size());
			fw.write(a.remove(randomNo) + "\n");
		}
		
		fw.flush();
		fw.close();
		
		System.out.println("Shuffled");
	}
	
	public static void divideSet(String datasetFile, String trainingFile, String testingFile, int trainingPer, int testingPer) throws IOException
	{
		if(trainingPer + testingPer != 100)
		{
			System.out.println("Sorry, the percentages do not add up to 100!");
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(datasetFile));
		ArrayList<String> a = new ArrayList<String>();
		
		while(br.ready())
		{
			a.add(br.readLine());
		}
		
		int totalLines = a.size();
		
		int trainingLines = (int) (trainingPer / 100.0 * totalLines);
		int testingLines = totalLines - trainingLines;
		
		FileWriter fw1 = new FileWriter(new File(trainingFile), false);
		FileWriter fw2 = new FileWriter(new File(testingFile), false);
		
		for(int i = 0; i < trainingLines; i++)
		{
			fw1.write(a.remove(0) + "\n");
		}
		
		while(!a.isEmpty())
		{
			fw2.write(a.remove(0) + "\n");
		}
		
		fw1.flush();
		fw2.flush();
		fw1.close();
		fw2.close();
		br.close();
		
	}
	
	
	public static void divideSet() throws IOException
	{
		
		BufferedReader br = new BufferedReader(new FileReader("/Users/omarali/Documents/Bachelor/DataSets/DataSetShuffled.txt"));
		ArrayList<String> a = new ArrayList<String>();
		
		while(br.ready())
		{
			a.add(br.readLine());
		}
		
		int totalLines = a.size();
		
		int trainingLines = (int) (0.8 * totalLines);
		int testingLines = totalLines - trainingLines;
		
		FileWriter fw1 = new FileWriter(new File("/Users/omarali/Documents/Bachelor/DataSets/TrainingSet.txt"), false);
		FileWriter fw2 = new FileWriter(new File("/Users/omarali/Documents/Bachelor/DataSets/TestingSet.txt"), false);
		
		for(int i = 0; i < trainingLines; i++)
		{
			fw1.write(a.remove(0) + "\n");
		}
		
		while(!a.isEmpty())
		{
			fw2.write(a.remove(0) + "\n");
		}
		
		fw1.flush();
		fw2.flush();
		fw1.close();
		fw2.close();
		br.close();
		
	}
	
	public static void divideToSets() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/omarali/Documents/Bachelor/DataSets/DataSetShuffled.txt"));
		ArrayList<String> a = new ArrayList<String>();
		
		while(br.ready())
		{
			a.add(br.readLine());
		}
		
		for(int i = 0; i < 5; i++)
		{
		
			int totalLines = a.size();
			
			int minWindow = (int) (totalLines * 0.2 * i);
			int maxWindow = (int) (totalLines * 0.2 * (i + 1));
			
			FileWriter fw1 = new FileWriter(new File("/Users/omarali/Documents/Bachelor/DataSets/ExperimentationDataSetsAll/TrainingSet" + i + ".txt"), false);
			FileWriter fw2 = new FileWriter(new File("/Users/omarali/Documents/Bachelor/DataSets/ExperimentationDataSetsAll/TestingSet" + i + ".txt"), false);
			
			for(int j = 0; j < totalLines; j++)
			{
				if(j >= minWindow && j < maxWindow)
					fw2.write(a.get(j) + "\n");
				else fw1.write(a.get(j) + "\n");
			}
				
			fw1.flush();
			fw2.flush();
			fw1.close();
			fw2.close();
			
		}
		
		br.close();
	}
	
	
}
