
public class RastriginFunction implements Function
{
	private final int dim;

	public RastriginFunction(int dim)
	{
		this.dim = dim;
	}

	public int getNumParams()
	{
		return dim;
	}

	public double getVal(double... x)
	{
		double sum = 0.0;

		for(int i = 0; i < dim; i++)
		{
			double xi = x[i];
			sum += xi * xi - 10.0*Math.cos(2.0*Math.PI*xi);
		}

		return sum+10.0*dim;
	}

	public void getimg(boolean val)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}