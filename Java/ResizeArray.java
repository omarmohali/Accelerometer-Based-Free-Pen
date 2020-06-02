import java.util.ArrayList;
import java.util.Arrays;


public class ResizeArray {

	public static double[] resize(ArrayList<Double> al, int size)
	{
		double[] a = new double[size];
		int alSize = al.size();
		double step = alSize / (size * 1.0);
		double j = 0;
		
		for(int i = 0; i < size; i++)
		{
			a[i] = al.get((int)j);
			j += step;
		}
		
		return a;
	}
}
