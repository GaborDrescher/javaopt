import java.util.Arrays;
import java.util.Random;

public final class DownhillSimplexSearch extends MinSearch
{
	private double a;
	private double g;
	private double p;
	private double o;
	private int maxInternIter;
	private double delta;
	private Random rand;

	public DownhillSimplexSearch(double a, double g, double p, double o, int maxInternIter, double delta, Random rand)
	{
		this.a = a;
		this.g = g;
		this.p = p;
		this.o = o;
		this.maxInternIter = maxInternIter;
		this.delta = delta;
		this.rand = rand;
	}

	public DownhillSimplexSearch(int maxInternIter, double delta, Random rand)
	{
		this(1.0, 2.0, 0.5, 0.5, maxInternIter, delta, rand);
	}

	public DownhillSimplexSearch(int maxInternIter, Random rand)
	{
		this(1.0, 2.0, 0.5, 0.5, maxInternIter, 0.000001, rand);
	}

	@Override
	protected double[] getMinimumImpl(final Function f)
	{
		final int dim = f.getNumParams();
		final PointComp comp = new PointComp(f);

		final Point[] currPoints = new Point[dim + 1];
		for(int i = 0; i < dim + 1; i++)
		{
			double[] data = new double[dim];
			for(int k = 0; k < dim; k++)
			{
				data[k] = rand.nextDouble();
			}
			currPoints[i] = new Point(data);
		}

		for(int it = 0;; ++it)
		{
			Arrays.sort(currPoints, comp);

			if(it > maxInternIter)
			{
				break;
			}

			double sum = 0.0;
			for(int k = 0; k < dim; k++)
			{
				final double diff = currPoints[0].data[k] - currPoints[1].data[k];
				sum += diff * diff;
			}
			
			if(Math.sqrt(sum) <= delta)
			{
				break;
			}

			//gravity of all points except x_{n+1}
			final Point x0 = new Point(new double[dim]);

			for(int i = 0; i < dim; i++)//YES dim and not currPoints.length
			{
				final double[] data = currPoints[i].data;

				for(int k = 0; k < dim; k++)
				{
					x0.data[k] += data[k];
				}
			}

			final double numinv = 1.0 / dim;//YES dim and not currPoints.length

			for(int k = 0; k < dim; k++)
			{
				x0.data[k] *= numinv;
			}
			//

			//reflection
			final Point xlast = currPoints[dim];
			final Point xr = new Point(new double[dim]);
			for(int k = 0; k < dim; k++)
			{
				xr.data[k] = x0.data[k] + a * (x0.data[k] - xlast.data[k]);
			}

			final double fx1 = currPoints[0].evaluate(f);
			final double fxr = xr.evaluate(f);
			final double fxn = currPoints[dim - 1].evaluate(f);

			if(fx1 <= fxr && fxr < fxn)
			{
				currPoints[dim] = xr;
				continue;
			}
			//

			//expansion
			if(fxr < fx1)
			{
				final Point xe = new Point(new double[dim]);
				for(int k = 0; k < dim; k++)
				{
					xe.data[k] = x0.data[k] + g * (x0.data[k] - xlast.data[k]);
				}

				final double fxe = xe.evaluate(f);

				if(fxe < fxr)
				{
					currPoints[dim] = xe;
					continue;
				}
				else
				{
					currPoints[dim] = xr;
					continue;
				}
			}
			//
			else
			{
				//contraction (here it is certain that fxr >= fxn)
				final Point xc = new Point(new double[dim]);
				for(int k = 0; k < dim; k++)
				{
					xc.data[k] = xlast.data[k] + p * (x0.data[k] - xlast.data[k]);
				}

				final double fxc = xc.evaluate(f);
				final double fxlast = xlast.evaluate(f);

				if(fxc < fxlast)
				{
					currPoints[dim] = xc;
					continue;
				}
				//
				else
				{
					//reduction
					for(int i = 1; i < dim; i++)
					{
						final Point repl = new Point(new double[dim]);
						for(int k = 0; k < dim; k++)
						{
							repl.data[k] = currPoints[0].data[k] + o * (currPoints[i].data[k] - currPoints[0].data[k]);
						}

						currPoints[i] = repl;
					}
					continue;
					//
				}
			}
		}

		return currPoints[0].data;
	}
}