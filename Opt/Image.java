import java.awt.Color;
import java.awt.image.BufferedImage;

public final class Image
{
	public final float[] data;
	public final int w;
	public final int h;

	public final float[] refData;

	//public final int gridsize;
	//public final float[] fitVals;
	//public final int gridw;
	//public final int gridh;
	public float fitness;


	private int getPixelAddress(int x, int y, int z)
	{
		//return w*4*y+4*x+z;
		return ((w << 2) * y) + (x << 2) + z;
	}

	private int getPixelAddress(int x, int y)
	{
		//return w*4*y+4*x;
		return ((w << 2) * y) + (x << 2);
	}

	public Image(Image refImg, int gridsize)
	{
		this.w = refImg.w;
		this.h = refImg.h;
		refData = refImg.data;

		data = new float[h*w*4];

		fitness = 0.0f;
		for(int i = 0; i < refData.length; i++)
		{
			fitness += refData[i];
		}
	}

	public Image(final BufferedImage refImg)
	{
		this.w = refImg.getWidth();
		this.h = refImg.getHeight();
		data = new float[h*w*4];

		refData = null;

		final float inv255 = 1.0f / 255.0f;

		for(int i = 0; i < h; i++)
		{
			for(int k = 0; k < w; k++)
			{
				final int adr = getPixelAddress(k, i);
				final Color c = new Color(refImg.getRGB(k, i));

				data[adr] = c.getAlpha()*inv255;
				data[adr+1] = c.getRed()*inv255;
				data[adr+2] = c.getGreen()*inv255;
				data[adr+3] = c.getBlue()*inv255;
			}
		}

	}

	public float getCurrentFitness()
	{
		return fitness;
	}

	public float getFitness(final Box box)
	{
		//calc box color
		box.color[0] = 0.0f;
		box.color[1] = 0.0f;
		box.color[2] = 0.0f;
		box.color[3] = 0.0f;

		for(int i = box.ymin; i < box.ymax; i++)
		{
			for(int k = box.xmin; k < box.xmax; k++)
			{
				final int adr = getPixelAddress(k, i);

				box.color[0] += refData[adr];
				box.color[1] += refData[adr+1];
				box.color[2] += refData[adr+2];
				box.color[3] += refData[adr+3];
			}
		}

		final float invsize = 1.0f/((box.xmax - box.xmin) * (box.ymax - box.ymin));

		box.color[0] *= invsize;
		box.color[1] *= invsize;
		box.color[2] *= invsize;
		box.color[3] *= invsize;
		//

		float sum = 0.0f;
		final float[] color = box.color;

		for(int i = 0; i < h; i++)
		{
			for(int k = 0; k < w; k++)
			{
				final int adr = getPixelAddress(k, i);

				if(i >= box.ymin && i <= box.ymax && k >= box.xmin && k<= box.xmax)
				{
					final float alpha = color[0] + data[adr]*(1.0f - color[0]);
					final float r = (color[1] * color[0] + data[adr+1] * data[adr+1]*(1.0f-color[0])) / alpha;
					final float g = (color[2] * color[0] + data[adr+2] * data[adr+2]*(1.0f-color[0])) / alpha;
					final float b = (color[3] * color[0] + data[adr+3] * data[adr+3]*(1.0f-color[0])) / alpha;

					sum += Math.abs(refData[adr] - alpha)
					+Math.abs(refData[adr+1] - r)
					+Math.abs(refData[adr+2] - g)
					+Math.abs(refData[adr+3] - b);
				}
				else
				{
					sum += Math.abs(refData[adr] - data[adr])
					+Math.abs(refData[adr+1] - data[adr+1])
					+Math.abs(refData[adr+2] - data[adr+2])
					+Math.abs(refData[adr+3] - data[adr+3]);
				}
			}
		}

		return sum;
	}

	public void draw(final Box box)
	{
		final float[] color = box.color;

		for(int i = box.ymin; i < box.ymax; i++)
		{
			for(int k = box.xmin; k < box.xmax; k++)
			{
				final int adr = getPixelAddress(k, i);

				final float alpha = color[0] + data[adr]*(1.0f - color[0]);

				final float r = (color[1] * color[0] + data[adr+1] * data[adr+1]*(1.0f-color[0])) / alpha;
				final float g = (color[2] * color[0] + data[adr+2] * data[adr+2]*(1.0f-color[0])) / alpha;
				final float b = (color[3] * color[0] + data[adr+3] * data[adr+3]*(1.0f-color[0])) / alpha;

				data[adr] = alpha;
				data[adr+1] = r;
				data[adr+2] = g;
				data[adr+3] = b;
			}
		}
	}

	public Color getColor(int x, int y)
	{
		int adr = getPixelAddress(x, y);
		Color c = new Color(data[adr+1],data[adr+2],data[adr+3],data[adr]);
		return c;
	}

	public void writeToImage(BufferedImage img)
	{
		for(int i = 0; i < h; i++)
		{
			for(int k = 0; k < w; k++)
			{
				img.setRGB(k, i, getColor(k, i).getRGB());
			}
		}
	}
}