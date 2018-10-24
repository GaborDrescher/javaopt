import java.util.BitSet;
import java.util.Comparator;

public abstract class MinSearch
{
	private BitSet bitset;
	private double[] vals;

	public MinSearch()
	{
		bitset = null;
		vals = null;
	}

	public MinSearch(BitSet bitset, double[] vals)
	{
		this.bitset = bitset;
		this.vals = vals;
	}

	public void setBitset(BitSet bitset, double[] vals)
	{
		this.bitset = bitset;
		this.vals = vals;
	}

	public double[] getMinimum(final Function f)
	{
		final int dim = f.getNumParams();
		Function appFunc = f;
		if(bitset == null)
		{
			return getMinimumImpl(appFunc);
		}

		final int card = bitset.cardinality();

		appFunc = new Function()
		{
			public int getNumParams()
			{
				return card;
			}

			public double getVal(double... x)
			{
				double[] in = new double[dim];
				for(int i = 0, i2 = 0; i < dim; i++)
				{
					if(bitset.get(i))
					{
						in[i] = x[i2];
						++i2;
					}
					else
					{
						in[i] = vals[i];
					}
				}

				return f.getVal(in);
			}

			public void getimg(boolean val)
			{
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};


		double[] min = getMinimumImpl(appFunc);
		double[] out = new double[dim];
		for(int i = 0, i2 = 0; i < dim; i++)
		{
					if(bitset.get(i))
					{
						out[i] = min[i2];
						++i2;
					}
					else
					{
						out[i] = vals[i];
					}
		}


		return out;
	}

	protected abstract double[] getMinimumImpl(Function f);

	protected static void clamp(final double[] data)
	{
		final int dim = data.length;
		for(int i = 0; i < dim; i++)
		{
			final double d = data[i];

			if(d > 1.0)
			{
				data[i] = 1.0;
			}
			else if(d < 0.0)
			{
				data[i] = 0.0;
			}
		}
	}

	protected final class Point
	{
		public final double[] data;
		public boolean isEvaluated;
		public double value;

		public Point(double[] data)
		{
			this.data = data;
			isEvaluated = false;
			value = 0.0;
		}

		public Point(int dim)
		{
			this(new double[dim]);
		}

		public double evaluate(Function f)
		{
			if(!isEvaluated)
			{
				clamp(data);
				value = f.getVal(data);
				isEvaluated = true;
			}

			return value;
		}
	}

	protected final class PointComp implements Comparator<Point>
	{
		private final Function f;

		public PointComp(final Function f)
		{
			this.f = f;
		}

		public int compare(final Point a, final Point b)
		{
			final double va = a.evaluate(f);
			final double vb = b.evaluate(f);

			if(va < vb)
			{
				return -1;
			}
			else if(va > vb)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
}
