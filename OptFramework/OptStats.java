public class OptStats
{
	public final Function function;
	public final double[] pos;
	public double value;
	public int numIter;
	public double error;
	
	public final int maxIter;
	public final double maxError;
	public final double[] guess;

	public OptStats(Function function, double[] guess, int maxIter, double maxError)
	{
		this.function = function;
		this.maxIter = maxIter;
		this.maxError = maxError;
		this.guess = new double[guess.length];
		System.arraycopy(guess, 0, this.guess, 0, guess.length);
		
		this.pos = new double[guess.length];
		this.value = Double.MAX_VALUE;
		this.numIter = 0;
		this.error = Double.MAX_VALUE;
	}
	
	public OptStats copy()
	{
		OptStats stats = new OptStats(function, guess, maxIter, maxError);
		stats.value = value;
		stats.numIter = numIter;
		stats.error = error;
		System.arraycopy(pos, 0, stats.pos, 0, pos.length);
		return stats;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("Function: ");
		builder.append(function.toString());
		builder.append('\n');
		
		builder.append("Optimum position: ");
		builder.append('<');
		for(int i = 0; i < pos.length-1; ++i)
		{
			builder.append(pos[i]);
			builder.append(", ");
		}
		builder.append(pos[pos.length-1]);
		builder.append(">\n");
		
		builder.append("Optimum value: ");
		builder.append(value);
		builder.append('\n');
		
		builder.append("Iterations: ");
		builder.append(numIter);
		builder.append('\n');
		
		builder.append("Error: ");
		builder.append(error);
		builder.append('\n');
		
		return builder.toString();
	}
}
