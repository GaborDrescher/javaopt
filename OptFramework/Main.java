import java.util.Random;

public class Main
{

	public static void mains(String[] args)
	{
		class CallB implements OptCallback
		{
			@Override
			public void call(OptStats stats)
			{
				System.out.println(stats.value);
			}
		}
		
		
		final int dim = 2;
		
		Random rand = new Random();
		Optimizer opt = new PSO(3000, rand);
		//Optimizer opt = new SamplingMetaOpt(opt1, 1000);
		
		Function func = new BananaFunction();
		
		double[] guess = new double[dim];//all elements zero
		
		
		OptStats stats = new OptStats(func, guess, 1000, 0.1);
		
		opt.optimize(stats, new CallB());
		
		System.out.println(stats);
		System.out.println("Evals: " + func.getNumEvals());
		
		func.resetEvals();
	}
}
