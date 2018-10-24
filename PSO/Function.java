import java.util.concurrent.atomic.AtomicInteger;

public abstract class Function
{
	private AtomicInteger aint;

	public Function()
	{
		aint = new AtomicInteger();
	}
	
	public int getEvalCount()
	{
		return aint.get();
	}
	
	public double eval(double[] in)
	{
		aint.incrementAndGet();
		return evalImpl(in);
	}
	
	protected abstract double evalImpl(double[] in);
	public abstract void getRanges(double[] min, double[] max);
	public abstract int getDimension();
}