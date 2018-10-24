import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class DHSimplex implements Optimizer
{
	private double a;
	private double g;
	private double p;
	private double o;
	private Random rand;

	public DHSimplex(double a, double g, double p, double o, Random rand)
	{
		this.a = a;
		this.g = g;
		this.p = p;
		this.o = o;
		this.rand = rand;
	}

	public DHSimplex(double delta, Random rand)
	{
		this(1.0, 2.0, 0.5, 0.5, rand);
	}

	public DHSimplex(Random rand)
	{
		this(1.0, 2.0, 0.5, 0.5, rand);
	}

	private void gravity(final FunctionWrapper[] currPoints, double[] x0)
	{
		final int dim = currPoints.length - 1;

		for(int i = 0; i < dim; ++i)//YES dim and not currPoints.length
		{
			x0[i] = 0.0;
			
			for(int k = 0; k < dim; k++)
			{
				x0[k] += currPoints[i].getData(k);
			}
		}

		final double numinv = 1.0 / dim;//YES dim and not currPoints.length

		for(int k = 0; k < dim; k++)
		{
			x0[k] *= numinv;
		}
	}
	
	@Override
	public void optimize(final OptStats stats, final OptCallback callback)
	{
		final Function func = stats.function;
		final int dim = func.numArgs();
		
		Comparator<FunctionWrapper> comp = new Comparator<FunctionWrapper>()
		{
			@Override
			public int compare(FunctionWrapper a, FunctionWrapper b)
			{
				final double aval = a.evaluate();
				final double bval = b.evaluate();
				
				if(aval < bval)
					return -1;
				
				if(aval > bval)
					return 1;
				
				return 0;
			}
		};
		
		final FunctionWrapper[] currPoints = new FunctionWrapper[dim+1];
		
		for(int i = 0; i < currPoints.length; ++i)
		{
			FunctionWrapper wrp = new FunctionWrapper(func);
			for(int k = 0; k < dim; k++)
			{
				if(Math.abs(stats.guess[k]) < 0.00025)
				{
					wrp.setData(k, (rand.nextDouble() * 2.0 - 1.0) * 0.00025);
				}
				else
				{
					wrp.setData(k, stats.guess[k] + (rand.nextDouble() * 2.0 - 1.0) * (stats.guess[k] /** (1.0 / 2.0)*/));
				}
			}
			currPoints[i] = wrp;
		}

		
		final double[] x0 = new double[dim];
		FunctionWrapper xr = new FunctionWrapper(func);
		FunctionWrapper xe = new FunctionWrapper(func);
		FunctionWrapper xc = new FunctionWrapper(func);
		for(int it = 0;; ++it)
		{
			Arrays.sort(currPoints, comp);
			
			double sum = 0.0;
			for(int k = 0; k < dim; k++)
			{
				final double diff = currPoints[0].getData(k) - currPoints[1].getData(k);
				sum += diff * diff;
			}
			sum = Math.sqrt(sum);
			
			++stats.numIter;
			stats.error = sum;
			stats.value = currPoints[0].evaluate();
			currPoints[0].copyInto(stats.pos);
			
			if(callback != null)
			{
				callback.call(stats);
			}
			
			if(stats.maxError > 0)
			{
				if(sum <= stats.maxError)
				{
					return;
				}
			}
			
			if(it >= stats.maxIter)
			{
				return;
			}

			//gravity of all points except x_{n+1}
			gravity(currPoints, x0);
			//

			//reflection
			for(int k = 0; k < dim; k++)
			{
				xr.setData(k, x0[k] + a * (x0[k] - currPoints[dim].getData(k)));
			}

			final double fx1 = currPoints[0].evaluate();
			final double fxr = xr.evaluate();
			final double fxn = currPoints[dim - 1].evaluate();

			if(fx1 <= fxr && fxr < fxn)
			{
				currPoints[dim].assign(xr);
				continue;
			}
			//

			//expansion
			if(fxr < fx1)
			{
				for(int k = 0; k < dim; k++)
				{
					xe.setData(k, x0[k] + g * (x0[k] - currPoints[dim].getData(k)));
				}

				final double fxe = xe.evaluate();

				if(fxe < fxr)
				{
					currPoints[dim].assign(xe);
					continue;
				}
				else
				{
					currPoints[dim].assign(xr);
					continue;
				}
			}
			//
			else
			{
				//contraction (here it is certain that fxr >= fxn)
				for(int k = 0; k < dim; k++)
				{
					xc.setData(k, currPoints[dim].getData(k) + p * (x0[k] - currPoints[dim].getData(k)));
				}

				final double fxc = xc.evaluate();
				final double fxlast = currPoints[dim].evaluate();

				if(fxc < fxlast)
				{
					currPoints[dim].assign(xc);
					continue;
				}
				//
				else
				{
					//reduction
					for(int i = 1; i < dim; i++)
					{
						FunctionWrapper current = currPoints[i];
						for(int k = 0; k < dim; k++)
						{
							current.setData(k, currPoints[0].getData(k) + o * (currPoints[i].getData(k) - currPoints[0].getData(k)));
						}
					}
					continue;
					//
				}
			}
		}
	}
}
