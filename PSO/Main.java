import java.util.Random;

public class Main
{
	public static void main(String[] args)
	{
		int popsize = 20;
		int maxiter = 10000;
		
		PSO pso = new PSO(0.99, 0.2, 0.2, popsize);
		Random rand = new Random();
		
		Function fun = new Rastrigin();
		
		double minval = 0.00001;
		
		
		double[] best = pso.minimize(fun, maxiter, rand);
		System.out.println("eval called "+fun.getEvalCount()+" times");

		
	}
}