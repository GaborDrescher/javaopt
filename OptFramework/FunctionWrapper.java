public final class FunctionWrapper
{
	private final Function function;
	private final double[] data;
	
	private double lastVal;
	private boolean validLastVal;

	public FunctionWrapper(Function function)
	{
		this.function = function;
		this.data = new double[function.numArgs()];
		this.lastVal = 0.0;
		this.validLastVal = false;
	}
	
	public double evaluate()
	{
		if(!validLastVal)
		{
			validLastVal = true;
			lastVal = function.evaluate(data);
		}
		
		return lastVal;
	}
	
	public void setData(int index, double value)
	{
		validLastVal = false;
		data[index] = value;
	}
	
	public double getData(int index)
	{
		return data[index];
	}
	
	public void assign(final FunctionWrapper other)
	{
		validLastVal = false;
		System.arraycopy(other.data, 0, data, 0, other.data.length);
	}
	
	public void copyInto(double[] outdata)
	{
		System.arraycopy(data, 0, outdata, 0, data.length);
	}
}
