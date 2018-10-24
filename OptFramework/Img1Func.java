import java.awt.image.BufferedImage;

public class Img1Func extends Function
{
	private BufferedImage im1;
	private BufferedImage im2;
	private double smoothFactor;
	
	public Img1Func(BufferedImage im1, BufferedImage im2, double smoothFactor)
	{
		if(im1.getWidth() * im1.getHeight() != im2.getWidth() * im2.getHeight())
		{
			throw new RuntimeException("image sizes must match");
		}
		
		this.im1 = im1;
		this.im2 = im2;
		this.smoothFactor = smoothFactor;
	}
	
	private double colorSimilarity(int rgb1, int rgb2)
	{
		double r1 = (rgb1 >> 16) & 0xff;
		double g1 = (rgb1 >> 8) & 0xff;
		double b1 = (rgb1 >> 0) & 0xff;
		
		double r2 = (rgb2 >> 16) & 0xff;
		double g2 = (rgb2 >> 8) & 0xff;
		double b2 = (rgb2 >> 0) & 0xff;
		
		double y1 = 0.299 * r1 + 0.587*g1+0.114*b1;
		double u1 = (b1-y1)*0.493;
		double v1 = (r1-y1)*0.877;
		
		double y2 = 0.299 * r2 + 0.587*g2+0.114*b2;
		double u2 = (b2-y2)*0.493;
		double v2 = (r2-y2)*0.877;
		
		double dx = y1-y2;
		double dy = u1-u2;
		double dz = v1-v2;
		
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	
	@Override
	public double evaluateImpl(double[] args)
	{
		final int w = im1.getWidth();
		final int h = im1.getHeight();
		
		double colorsum = 0;
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				final int index = (y*w+x)*2;
				
				double xf = args[index];
				double yf = args[index+1];
				
				//nearest neighbor color, use xf+x and yf+y, TODO -> bilinear
				int idxX = (int)Math.rint(xf + x);
				int idxY = (int)Math.rint(yf + y);
				idxX = idxX < 0 ? 0 : (idxX >= w ? (w-1) : idxX);
				idxY = idxY < 0 ? 0 : (idxY >= h ? (h-1) : idxY);
				
				
				int rgb2 = im2.getRGB(idxX, idxY);
				int rgb1 = im1.getRGB(x, y);
				colorsum += colorSimilarity(rgb1, rgb2);
			}
		}
		
		double smoothsum = 0;
		for(int y = 1; y < h-1; y++)
		{
			for(int x = 1; x < w-1; x++)
			{
				final int index = (y*w+x)*2;
				
				double xf = args[index];
				double yf = args[index+1];
				
				for(int i = 0; i < 3; i++)
				{
					for(int k = 0; k < 3; k++)
					{
						if(i == 1 && k == 1) continue;
						
						final int indexo = ((y+(i-1))*w+(x+(k-1)))*2;
						
						double xo = args[indexo];
						double yo = args[indexo+1];
						
						double dx = xo - xf;
						double dy = yo - yf;
						smoothsum += Math.sqrt(dx*dx+dy*dy);
					}
				}
				
				
			}
		}
		
		return colorsum + smoothFactor * smoothsum;
	}

	@Override
	public int numArgs()
	{
		return im1.getWidth() * im2.getHeight() * 2;
	}
	
}
