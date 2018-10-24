import java.util.Random;

public class Main
{
	public static void main(String[] args)
	{
		int dim = 20;

		long seed = System.currentTimeMillis();
		Random rand = new Random(seed);

		//BananaFunction f = new BananaFunction(dim);
		NeuronTestFunction f = new NeuronTestFunction(3, 20, 3);

		//MinSearch s = new GDOptimizer(new ISOptimizer(new DownhillSimplexSearch(10000, 0.00001, rand), 1), 4);
		//MinSearch s = new ISOptimizer(new DownhillSimplexSearch(10000, 0.0000001, rand), 1000);
		MinSearch s = new PSOptimizer(2, 2, 1, 0.1, 100, 10000, 0.0, rand);

		long time = System.currentTimeMillis();
		double[] min = s.getMinimumImpl(f);
		time = System.currentTimeMillis() - time;
		System.out.println(time);
		System.out.println();
		

		for(int i = 0; i < min.length; i++)
		{
			System.out.println(min[i]);
		}

		System.out.println();
		f.print = true;
		System.out.println(f.getVal(min));
	}
}