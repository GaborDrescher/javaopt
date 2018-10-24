import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
public class NeuronTestFunction implements Function
{
	private NeuroSys sys;
	public boolean print;
	private boolean outimg = false;

	BufferedImage in = null;

	public NeuronTestFunction(int ... layers)
	{
		
		try
		{
			in = ImageIO.read(new File("test.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		this.sys = new NeuroSys(layers);
	}

	public int getNumParams()
	{
		return sys.getNumConnections();
	}

	public double getVal(double... x)
	{
		final double factor = 20.0;

		double[][][] data = sys.getAllConnections();

		int index = 0;
		for(int i = 0; i < data.length; i++)
		{
			double[][] dses = data[i];
			for(int k = 0; k < dses.length; k++)
			{
				double[] d2 = dses[k];
				for(int j = 0; j < d2.length; j++, index++)
				{
					d2[j] = x[index] * factor - factor * 0.5;
				}
			}
		}

		return getFitness();
	}

	private static float clamp(double d)
	{
		double val = Math.abs(d);

		if(val >= 1.0) return (float)1.0;
		return (float)val;
	}

	public void getimg(boolean val)
	{
		outimg = val;
	}

	private double getFitness()
	{
		

		BufferedImage out = null;
		if(outimg) out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

		double[] input = new double[3];
		input[0] = 1.0;

		double sum = 0.0;

		for(int y = 0; y < in.getHeight(); y++)
		{
			for(int x = 0; x < in.getWidth(); x++)
			{
				input[1] = x/(double)in.getWidth();
				input[2] = y/(double)in.getHeight();

				double[] state = sys.tick(input);

				Color col = new Color(clamp(state[0]), clamp(state[1]), clamp(state[2]));
				if(outimg) out.setRGB(x, y, col.getRGB());
				Color c2 = new Color(in.getRGB(x, y));
				double diffr = (col.getRed() - c2.getRed())/255.0;
				double diffg = (col.getGreen() - c2.getGreen())/255.0;
				double diffb = (col.getBlue() - c2.getBlue())/255.0;
				sum += diffr*diffr+diffg*diffg+diffb*diffb;
			}
		}

		if(outimg)
		{
			try
			{
				ImageIO.write(out, "png", new File("out/test_out"+System.currentTimeMillis()+".png"));
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}

		return sum;


			/*
			//final char[] ref = "Hallo mein suesser Schatz.".toCharArray();
			final char[] ref = "Meike liebt Gabor".toCharArray();
			//
			double[] input = new double[2];
			double fitness = 0.0;
			for(int i = 0; i < ref.length; i++)
			{
			double inval = i/((double)(ref.length-1));
			input[0] = inval;
			input[1] = 1.0;
			double[] state = sys.tick(input);
			char out = 32;
			if(state != null)
			{
			double outval = Math.abs(state[0]);
			out = (char)Math.max(((int)Math.round(outval * 94)) + 32, 0);
			}
			double diff = ref[i] - out;
			fitness += diff * diff;
			if(print) System.out.print(out);
			}
			if(print) System.out.println();
			return fitness;*/
		

	}
}