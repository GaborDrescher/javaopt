import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main2
{
	private static JFrame frame = null;
	private static Image oimg = null;
	private static final Object lock = new Object();

	public static void gui()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(){

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				synchronized(lock)
				{
					BufferedImage bi = new BufferedImage(oimg.w, oimg.h, BufferedImage.TYPE_INT_ARGB);
					Graphics gr = bi.getGraphics();
					gr.setColor(Color.black);
					bi.getGraphics().drawRect(0, 0, oimg.w, oimg.h);
					oimg.writeToImage(bi);
					g.drawImage(bi, 0, 0, null);
				}
			}

			@Override
			public Dimension getPreferredSize()
			{
				return new Dimension(oimg.w, oimg.h);
			}
		};

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main_(String[] args)
	{
		BufferedImage bi = null;

		try
		{
			bi = ImageIO.read(new File("test.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.exit(-1);
		}

		final BufferedImage input = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
		input.getGraphics().drawImage(bi, 0, 0, null);
		bi = null;

		Image inimg = new Image(input);

		oimg = new Image(inimg, 0);

		gui();

		final long seed = 1337;//System.currentTimeMillis();
		System.out.println("seed: "+seed);
		Random rand = new Random(seed);
		ImgFunction f = new ImgFunction(oimg, oimg.w, oimg.h);
		MinSearch mins = new ISOptimizer(new DownhillSimplexSearch(100, 0.0001, rand), 2);
		//MinSearch mins = new SimpleDownhillSearch(rand, 10, 0.00001);
		//MinSearch mins = new SimpleRandomSearch(rand, 10);

		List<Box> list = new LinkedList<Box>();

		long time = System.currentTimeMillis();
		for(int i = 0; list.size() < 100; i++)
		{
			double[] x = mins.getMinimumImpl(f);

			Box box = new Box();

			box.setCoords(
			Math.round(ImgFunction.clamp((float)x[0]) * oimg.w),
			Math.round(ImgFunction.clamp((float)x[1]) * oimg.h),
			Math.round(ImgFunction.clamp((float)x[2]) * oimg.w),
			Math.round(ImgFunction.clamp((float)x[3]) * oimg.h)
			);

			float cfit = oimg.getCurrentFitness();
			float nfit = oimg.getFitness(box);

			if(nfit < cfit)
			{
				oimg.fitness = nfit;
				synchronized(lock)
				{
				oimg.draw(box);
				}
				list.add(box);
				frame.repaint();
				System.out.println(i+" "+nfit+" "+list.size());
			}
		}
		time = System.currentTimeMillis() - time;
		System.out.println("time: "+time);

		BufferedImage oi = new BufferedImage(oimg.w, oimg.h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = oi.getGraphics();
		g.setColor(Color.black);
		g.drawRect(0, 0, oimg.w, oimg.h);

		oimg.writeToImage(oi);
		try
		{
			ImageIO.write(oi, "png", new File("out.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
	}


	private static final class ImgFunction implements Function
	{
		public Image currentimg;
		public float w;
		public float h;

		public ImgFunction(Image currentimg, int w, int h)
		{
			this.w = w;
			this.h = h;
			this.currentimg = currentimg;
		}

		public int getNumParams()
		{
			return 4;
		}

		public static float clamp(final float x)
		{
			if(x >= 1.0f) return 1.0f;
			else if(x <= 0.0f) return 0.0f;
			else return x;
		}

		public double getVal(final double... x)
		{
			final Box box = new Box();

			box.setCoords(
			Math.round(clamp((float)x[0]) * w),
			Math.round(clamp((float)x[1]) * h),
			Math.round(clamp((float)x[2]) * w),
			Math.round(clamp((float)x[3]) * h)
			);

			return currentimg.getFitness(box);
		}

		public void getimg(boolean val)
		{
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
}
