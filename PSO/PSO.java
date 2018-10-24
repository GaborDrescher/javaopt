import java.util.Random;

public class PSO
{
	private final double w;
	private final double p;
	private final double g;
	private final int popsize;

	//TODO:
	//clamp velocity
	//detect stagnation (not always?)
	//regroup particles
	
	public PSO(double w, double p, double g, int popsize)
	{
		this.w = w;
		this.p = p;
		this.g = g;
		this.popsize = popsize;
	}

	private void copy(double[] src, double[] dest, int dim)
	{
		System.arraycopy(src, 0, dest, 0, dim);
	}
	
	public static String makeString(double[] vals)
	{
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		
		if(vals == null)
		{
			builder.append("null>");
			return builder.toString();
		}
		
		for(int i = 0; i < vals.length-1; ++i)
		{
			builder.append(vals[i]);
			builder.append(", ");
		}
		
		if(vals.length > 0)
		{
			builder.append(vals[vals.length-1]);
		}
		
		builder.append('>');
		return builder.toString();
	}
	
	private double getRandomInRange(double a, double b, Random rand)
	{
		double r = rand.nextDouble();// 0 to 1
		double smaller = a < b ? a : b;
		double larger = a > b ? a : b;
		return (larger - smaller) * r + smaller;
	}
	
	public double[] minimize(Function f, int maxiter, Random rand)
	{
		final int dim = f.getDimension();

		double bestfit = Double.MAX_VALUE;

		double[] rangeA = new double[dim];
		double[] rangeB = new double[dim];
		f.getRanges(rangeA, rangeB);
		
		double[][] pos = new double[popsize][dim];
		double[][] vel = new double[popsize][dim];
		double[][] bestpos = new double[popsize][dim];
		double[] bestval = new double[popsize];
		double[] best = new double[dim];

		//init
		for(int i = 0; i < popsize; ++i)
		{
			for(int k = 0; k < dim; ++k)
			{
				pos[i][k] = getRandomInRange(rangeA[k], rangeB[k], rand);
				bestpos[i][k] = pos[i][k];
				vel[i][k] = getRandomInRange(-Math.abs(rangeB[k] - rangeA[k]), Math.abs(rangeB[k] - rangeA[k]), rand);

				bestval[i] = Double.MAX_VALUE;
			}
		}
		
		int it = 0;
		for(; it < maxiter; ++it)
		{
			for(int i = 0; i < popsize; ++i)
			{
				double val = f.eval(pos[i]);
				if(val < bestval[i])
				{
					bestval[i] = val;
					copy(pos[i], bestpos[i], dim);
					if(val < bestfit)
					{
						bestfit = val;
						copy(pos[i], best, dim);
					}
				}
				
				for(int k = 0; k < dim; ++k)
				{
					double rp = rand.nextDouble();
					double rg = rand.nextDouble();
					
					vel[i][k] = w*vel[i][k] + p*rp*(bestpos[i][k] - pos[i][k]) + g*rg*(best[k] - pos[i][k]);
				}
				for(int k = 0; k < dim; ++k)
				{
					pos[i][k] += vel[i][k];
				}
			}
			
			if(it % 10 == 0)
				System.out.println(it + " " + bestfit + " " + makeString(best));
		}

		System.out.println(it + ": " + bestfit + " " + makeString(best));
		return best;
	}
}