public class Rastrigin extends Function
{
	//min at f(0,0,...) = 0
	@Override
	protected double evalImpl(double[] x)
	{
		if(x == null) return 0;
		
		int n = x.length;
		double A = 10;
		
		double sum = 0;
		for(int i = 0; i < n; ++i)
		{
			sum += x[i] * x[i] - A * Math.cos(2.0*Math.PI*x[i]);
		}
		sum += A*n;
		return sum;
	}
	
	@Override
	public void getRanges(double[] min, double[] max)
	{
		for(int i = 0; i < max.length; i++)
		{
			max[i] = 5.12;
		}
		
		for(int i = 0; i < min.length; i++)
		{
			min[i] = -5.12;
		}
	}
	
	@Override
	public int getDimension()
	{
		return 30;
	}
}