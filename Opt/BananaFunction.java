
public class BananaFunction implements Function
{

	private final int dim;
	private final double intervSize;

	public BananaFunction(int dim)
	{
		this.dim = dim;
		this.intervSize = 10.0;
	}

	public double getIntSize()
	{
		return intervSize;
	}

	public int getNumParams()
	{
		return dim;
	}

	public double getVal(double... x)
	{
		double sum = 0.0;

		for(int i = 0; i < dim-1; i++)
		{
			double xi = x[i] * intervSize;
			double xip1 = x[i+1] * intervSize;
			sum += 100.0 * (xip1 - xi*xi)*(xip1 - xi*xi) + (1.0 - xi)*(1.0 - xi);
		}

		return sum;
	}

	public void getimg(boolean val)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}