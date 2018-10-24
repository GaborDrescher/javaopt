
public final class DeJongFunction implements Function
{
	private final int dim;

	public DeJongFunction(int dim)
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
			sum += x[i] * x[i];
		}

		return sum;
	}

	public void getimg(boolean val)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}