public class Himmelblau extends Function
{
	//min at f(3,2) = 0
	//min at f(-2.805118, 3.131312) = 0
	//min at f(-3.779310, -3.283186) = 0
	//min at f(3.584428, -1.848126) = 0
	@Override
	protected double evalImpl(double[] in)
	{
		if(in == null || in.length != 2) return 0;
		
		double x = in[0];
		double y = in[1];
		
		return (x*x+y-11)*(x*x+y-11) + (x+y*y-7)*(x+y*y-7);
	}

	@Override
	public void getRanges(double[] min, double[] max)
	{
		for(int i = 0; i < max.length; i++)
		{
			max[i] = 5.0;
		}
		
		for(int i = 0; i < min.length; i++)
		{
			min[i] = -5.0;
		}
	}

	@Override
	public int getDimension()
	{
		return 2;
	}
}