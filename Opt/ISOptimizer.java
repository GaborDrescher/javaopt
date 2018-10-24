
public class ISOptimizer extends MinSearch
{
	private MinSearch opt;
	private int iter;

	public ISOptimizer(MinSearch opt, int iter)
	{
		this.opt = opt;
		this.iter = iter;
	}

	protected double[] getMinimumImpl(Function f)
	{
		double[] best = null;
		double bestval = Double.MAX_VALUE;

		for(int i = 0; i < iter; ++i)
		{
			double[] current = opt.getMinimumImpl(f);
			double val = f.getVal(current);
			if(val < bestval)
			{
				bestval = val;
				best = current;
			}
			System.out.println(i+" "+bestval);
		}

		return best;
	}
}
