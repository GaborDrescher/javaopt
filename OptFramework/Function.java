import java.util.concurrent.atomic.AtomicInteger;

public abstract class Function
{
	private AtomicInteger evalCount;

	public Function()
	{
		evalCount = new AtomicInteger(0);
	}
	
	public double evaluate(double[] args)
	{
		evalCount.incrementAndGet();
		return evaluateImpl(args);
	}
	
	public int getNumEvals()
	{
		return evalCount.get();
	}
	
	public void resetEvals()
	{
		evalCount.set(0);
	}
	
	public abstract double evaluateImpl(double[] args);
	public abstract int numArgs();
}