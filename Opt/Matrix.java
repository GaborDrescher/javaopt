
public class Matrix
{
	private double[] data;
	private int cols;
	private int rows;

	public Matrix(int rows, int cols)
	{
		this.cols = cols;
		this.rows = rows;
		data = new double[cols * rows];
	}

	public Matrix(Matrix other)
	{
		cols = other.cols;
		rows = other.rows;
		data = new double[other.data.length];
		System.arraycopy(other.data, 0, data, 0, data.length);
	}

	public static void mult(final Vector a, final Matrix b, final Vector c)
	{
		final int dimY = b.rows;
		final int dimX = b.cols;
		final double[] matrix = b.data;
		final double[] vector = c.data;
		final double[] outVector = a.data;

		for(int y = 0; y < dimY; ++y)
		{
			double sum = 0.0;
			final int ydimx = y * dimX;

			for(int x = 0; x < dimX; ++x)
			{
				sum += matrix[ydimx + x] * vector[x];
			}

			outVector[y] = sum;
		}
	}

	public void set(int x, int y, double d)
	{
		data[y * cols + x] = d;
	}

	public double get(int x, int y)
	{
		return data[y * cols + x];
	}

	public double[] getData()
	{
		return data;
	}
}
