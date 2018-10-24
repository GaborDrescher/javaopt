import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main3
{
	public static void main_(String[] args) throws IOException
	{
		BufferedImage im1 = ImageIO.read(new File("test.png"));
		BufferedImage im2 = ImageIO.read(new File("test.jpg"));

		int w = im1.getWidth();
		int h = im1.getHeight();

		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for(int i = 0; i < h; i++)
		{
			for(int k = 0; k < w; k++)
			{
				Color a = new Color(im1.getRGB(k, i));
				Color b = new Color(im2.getRGB(k, i));

				Color c = new Color(Math.abs(a.getRed()- b.getRed()), Math.abs(a.getGreen()- b.getGreen()), Math.abs(a.getBlue()- b.getBlue()), Math.abs(a.getAlpha()- b.getAlpha()));

				out.setRGB(k, i, c.getRGB());
			}
		}


		ImageIO.write(out, "png", new File("test_out.png"));

	}
}
