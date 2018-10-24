import java.util.Random;

public class PSOptimizer extends MinSearch
{
	private double c1;
	private double c2;
	private double w;
	private double vmax;
	private int numParticles;
	private int iter;
	private double error;
	private Random rand;

	public PSOptimizer(double c1, double c2, double w, double vmax, int numParticles, int iter, double error, Random rand)
	{
		this.c1 = c1;
		this.c2 = c2;
		this.w = w;
		this.vmax = vmax;
		this.numParticles = numParticles;
		this.iter = iter;
		this.error = error;
		this.rand = rand;
	}

	@Override
	protected double[] getMinimumImpl(Function f)
	{
		final int dim = f.getNumParams();
		Point[] particles = new Point[numParticles];
		double[][] pvel = new double[numParticles][dim];
		Point[] particlesBest = new Point[numParticles];
		double[] globalBest = new double[dim];

		double[] localBestVal = new double[numParticles];
		double globBestVal = Double.MAX_VALUE;

		for(int i = 0; i < numParticles; i++)
		{
			double[] data = new double[dim];
			double[] best = new double[dim];

			for(int k = 0; k < dim; k++)
			{
				data[k] = rand.nextDouble();
				best[k] = data[k];
			}

			particles[i] = new Point(data);
			particlesBest[i] = new Point(best);

			double currentVal = particlesBest[i].evaluate(f);
			localBestVal[i] = currentVal;
			if(currentVal < globBestVal)
			{
				globBestVal = currentVal;
				System.arraycopy(particlesBest[i].data, 0, globalBest, 0, dim);
			}

			for(int k = 0; k < dim; k++)
			{
				double[] cvel = pvel[i];
				for(int j = 0; j < dim; j++)
				{
					cvel[j] = 0.0;//rand.nextDouble() * 2.0 - 1.0;
					
				}
			}
		}

		for(int times = 0; times < iter; times++)
		{
			for(int i = 0; i < numParticles; i++)
			{
				double rp = rand.nextDouble();
				double rg = rand.nextDouble();

				double[] currentPos = particles[i].data;
				double[] currentVel = pvel[i];
				double[] partBest = particlesBest[i].data;

				//if(i==0)System.out.print(times+" ");
				for(int k = 0; k < dim; k++)
				{
					//if(i==0 && k == 0)System.out.print(currentPos[k]+" ");
					currentVel[k] = w * currentVel[k] + c1 * rp * (partBest[k] - currentPos[k]) + c2 * rg * (globalBest[k] - currentPos[k]);
					if(currentVel[k] > vmax) currentVel[k] = vmax;
					else if(currentVel[k] < -vmax) currentVel[k] = -vmax;

					//if(i==0 && k == 0)System.out.println(times +" "+Math.abs(currentVel[k] / vmax));

					currentPos[k] = currentPos[k] + currentVel[k];
				}
				//if(i==0)System.out.println();

			}
			//System.out.println();

			for(int i = 0; i < numParticles; i++)
			{
				particles[i].isEvaluated = false;
				double cfit = particles[i].evaluate(f);

				if(cfit < localBestVal[i])
				{
					localBestVal[i] = cfit;
					System.arraycopy(particles[i].data, 0, particlesBest[i].data, 0, dim);
				}

				if(cfit < globBestVal)
				{
					globBestVal = cfit;
					System.arraycopy(particles[i].data, 0, globalBest, 0, dim);
				}
			}

			/*
			f.getimg(true);
			f.getVal(globalBest);
			f.getimg(false);
			*/
			System.out.println(times+" "+globBestVal);

			if(globBestVal <= error) break;
		}

		return globalBest;
	}
}
