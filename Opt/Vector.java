public class Vector
{
	public double[] data;

	public Vector(int dim)
	{
		data = new double[dim];
	}

	public Vector(Vector other)
	{
		data = new double[other.data.length];
		System.arraycopy(other.data, 0, data, 0, data.length);
	}

	public static void plusEq(final Vector a, final Vector b)//a+=b
	{
		final double[] dataA = a.data;
		final double[] dataB = b.data;
		final int size = dataA.length;

		for(int i = 0; i < size; ++i)
		{
			dataA[i] += dataB[i];
		}
	}

	public static void swap(final Vector a, final Vector b)
	{
		double[] tmp = a.data;
		a.data = b.data;
		b.data = tmp;
	}

	public void set(double v)
	{
		for(int i = 0; i < data.length; i++)
		{
			data[i] = v;
		}
	}
}