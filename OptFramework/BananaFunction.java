public class BananaFunction extends Function
{
	@Override
	public double evaluateImpl(double[] x)
	{
		return (1.0 - x[0])*(1.0 - x[0]) + 100.0 * (x[1]-(x[0]*x[0]))* (x[1]-(x[0]*x[0]));
	}

	@Override
	public int numArgs()
	{
		return 2;
	}
}
