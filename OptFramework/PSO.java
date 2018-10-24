import java.util.Random;

public class PSO implements Optimizer
{
	private int numParticles;
	private Random rand;
	private double w;
	private double pg;

	public PSO(int numParticles, double w, double pg, Random rand)
	{
		this.numParticles = numParticles;
		this.rand = rand;
		this.w = w;
		this.pg = pg;
	}
	
	public PSO(int numParticles, Random rand)
	{
		this(numParticles, -0.4, 3.0, rand);
	}

	@Override
	public void optimize(OptStats stats, OptCallback callback)
	{
		class Particle
		{
			public FunctionWrapper pos;
			public double[] vel;

			public Particle(Function func)
			{
				pos = new FunctionWrapper(func);
				vel = new double[func.numArgs()];
			}
		}

		final Function func = stats.function;
		final int dim = func.numArgs();
		final int iter = stats.maxIter;

		FunctionWrapper globalBest = new FunctionWrapper(func);
		for(int k = 0; k < dim; ++k)
		{
			globalBest.setData(k, Double.MAX_VALUE);
		}

		Particle[] particles = new Particle[numParticles];

		for(int i = 0; i < particles.length; ++i)
		{
			particles[i] = new Particle(func);
			for(int k = 0; k < dim; ++k)
			{
				particles[i].pos.setData(k, stats.guess[k] + (rand.nextDouble() * 2.0 - 1.0) * stats.guess[k]);
				particles[i].vel[k] = (rand.nextDouble()*2.0 - 1.0)*0.5;
			}
		}
		
		for(int i = 0;; ++i)
		{
			//System.out.println("\t"+i);
			Particle c = particles[rand.nextInt(particles.length)];
			
			boolean bla = false;
			if(c.pos.evaluate() < globalBest.evaluate())
			{
				bla = true;
				globalBest.assign(c.pos);
			}
			
			if((i >= iter) || (bla))
			{
				stats.error = 0;//TODO
				stats.numIter = i;
				globalBest.copyInto(stats.pos);
				stats.value = globalBest.evaluate();
				
				if(callback != null)
				{
					callback.call(stats);
				}
				
				if(i >= iter)
				{
					return;
				}
			}
			
			for(int d = 0; d < dim; ++d)
			{
				double rg = rand.nextDouble();
				c.vel[d] = w * c.vel[d] + rg * pg * (globalBest.getData(d) - c.pos.getData(d));
			}
			
			//TODO clamp vel
			for(int d = 0; d < dim; ++d)
			{
				double pos = c.pos.getData(d);
				c.pos.setData(d, pos + c.vel[d]);
			}
		}

		
	}
}
