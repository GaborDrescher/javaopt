import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImgMain
{
	public static void main(String[] args)
	{
		class CallB implements OptCallback
		{
			@Override
			public void call(OptStats stats)
			{
				System.out.println(stats.value);
			}
		}
		
		
		BufferedImage im1 = null;
		BufferedImage im2 = null;
		try
		{
			im1 = ImageIO.read(new File("test/t1.png"));
			im2 = ImageIO.read(new File("test/t2.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}
		
		final double smoothFactor = 0.003;
		final int dim = im1.getHeight() * im1.getWidth() * 2;
		Random rand = new Random();
		Optimizer opt = new PSO(30, rand);
		//Optimizer opt = new DHSimplex(rand);
		//Optimizer opt = new SamplingMetaOpt(opt1, 1000);
		Function func = new Img1Func(im1, im2, smoothFactor);
		
		double[] guess = new double[dim];
		for(int i = 0; i < dim; ++i)
		{
			guess[i] = 0.0;
		}
		
		OptStats stats = new OptStats(func, guess, 1000, 0.1);
		
		opt.optimize(stats, new CallB());
		
		System.out.println("opt done");
		System.out.println(stats);
		System.out.println("Evals: " + func.getNumEvals());
		
		func.resetEvals();

		StringBuilder builder = new StringBuilder(10000);
		final int w = im1.getWidth();
		final int h = im1.getHeight();
		
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				builder.append(x);
				builder.append(' ');
				
				builder.append(y);
				builder.append(' ');
				
				builder.append(stats.pos[(y*w+x)*2]);
				builder.append(' ');
				
				builder.append(stats.pos[(y*w+x)*2 + 1]);
				builder.append('\n');
			}
		}
		
		try
		{
		File out = new File("out");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));
		byte[] b = builder.toString().getBytes();
		bos.write(b, 0, b.length);
		bos.flush();
		bos.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
