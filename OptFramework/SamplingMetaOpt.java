public class SamplingMetaOpt implements Optimizer
{
	private Optimizer opt;
	private int metaIter;

	public SamplingMetaOpt(Optimizer opt, int metaIter)
	{
		this.opt = opt;
		this.metaIter = metaIter;
	}
	
	@Override
	public void optimize(OptStats stats, OptCallback callback)
	{
		OptStats outstats = stats.copy();

		OptStats best = stats.copy();

		for(int i = 0; i < metaIter; i++)
		{
			OptStats tmp = stats.copy();
			opt.optimize(tmp, null);

			if(tmp.value < best.value)
			{
				best = tmp.copy();
				best.numIter = i;
				if(callback != null)
				{
					callback.call(best);
				}
			}
		}
		
		stats.error = best.error;
		stats.numIter = metaIter;
		stats.value = best.value;
		System.arraycopy(best.pos, 0, stats.pos, 0, stats.pos.length);
	}
}
