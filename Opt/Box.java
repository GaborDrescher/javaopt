
public final class Box
{
	public int xmin;
	public int ymin;
	public int xmax;
	public int ymax;

	public final float[] color;

	public Box()
	{
		color = new float[4];
	}

	public void setCoords(int x1, int y1, int x2, int y2)
	{
		xmax = x1;
		xmin = x2;
		ymax = y1;
		ymin = y2;

		if(xmin > xmax)
		{
			final int t = xmin;
			xmin = xmax;
			xmax = t;
		}

		if(ymin > ymax)
		{
			final int t = ymin;
			ymin = ymax;
			ymax = t;
		}
	}
}