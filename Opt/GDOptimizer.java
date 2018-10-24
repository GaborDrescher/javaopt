import java.util.BitSet;

public class GDOptimizer extends MinSearch
{
	private MinSearch opt;
	private int iter;

	public GDOptimizer(MinSearch opt, int iter)
	{
		this.opt = opt;
		this.iter = iter;
	}

	@Override
	protected double[] getMinimumImpl(Function f)
	{
		final int dim = f.getNumParams();
		double[] min = new double[dim];

		BitSet bitset = new BitSet(dim);

		for(int k = 0; k < iter; k++)
		{
			for(int i = 0; i < dim; i++)
			{
				bitset.clear();
				bitset.set(i);
				opt.setBitset(bitset, min);
				double[] current = opt.getMinimum(f);

				double cfit = f.getVal(current);
				double ofit = f.getVal(min);

				if(cfit < ofit)
				{
					System.arraycopy(current, 0, min, 0, dim);
				}

				System.out.println(k+" : "+i+"  "+Math.min(cfit, ofit));
			}
		}

		return min;
	}
}
