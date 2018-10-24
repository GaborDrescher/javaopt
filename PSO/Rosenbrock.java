public class Rosenbrock extends Function
{
	//min at f(1, 1) = 0
	@Override
	protected double evalImpl(double[] in)
	{		
		if(in == null || in.length != 2) return 0;
		
		double x = in[0];
		double y = in[1];
		
		return (1-x)*(1-x) + 100*(y-x*x)*(y-x*x);
	}

	@Override
	public void getRanges(double[] min, double[] max)
	{
		for(int i = 0; i < max.length; i++)
		{
			max[i] = 3;
		}
		
		for(int i = 0; i < min.length; i++)
		{
			min[i] = -3;
		}
	}
	
	@Override
	public int getDimension()
	{
		return 2;
	}
}