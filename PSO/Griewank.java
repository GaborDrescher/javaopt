public class Griewank extends Function
{
	//min at f(0,0,...) = 0
	@Override
	protected double evalImpl(double[] x)
	{
		if(x == null) return 0;
		
		int n = x.length;
		
		double sum = 0;
		for(int i = 0; i < n; ++i)
		{
			sum += x[i] * x[i];
		}
		sum /= 4000.0;
		
		double mult = 1;
		for(int i = 0; i < n; ++i)
		{
			mult *= Math.cos(x[i] / Math.sqrt(i+1));
		}
		
		return 1.0 + sum - mult;
	}

	@Override
	public void getRanges(double[] min, double[] max)
	{
		for(int i = 0; i < max.length; i++)
		{
			max[i] = 600.0;
		}
		
		for(int i = 0; i < min.length; i++)
		{
			min[i] = -600.0;
		}
	}

	@Override
	public int getDimension()
	{
		return 2;
	}
}