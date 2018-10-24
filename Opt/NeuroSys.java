
public class NeuroSys
{
	private double[][] allLayers;
	private double[][][] connectionMatrices;
	private int connections;

	public NeuroSys(int ... layerSizes)
	{
		if(layerSizes.length < 2)
		{
			throw new RuntimeException("need at least one input and one output layer");
		}

		allLayers = new double[layerSizes.length][];

		for(int i = 0; i < allLayers.length; i++)
		{
			allLayers[i] = new double[layerSizes[i]];
		}

		connectionMatrices = new double[layerSizes.length-1][][];

		for(int i = 0; i < layerSizes.length-1; i++)
		{
			connectionMatrices[i] = new double[layerSizes[i]][layerSizes[i+1]];
			connections += layerSizes[i] * layerSizes[i+1];
		}
	}

	private void singleStep(double[] fromLayer, double[][] connections, double[] toLayer)
	{
		for(int i = 0; i < toLayer.length; ++i)
		{
			toLayer[i] = 0.0;
		}

		for(int from = 0; from < fromLayer.length; ++from)
		{
			for(int to = 0; to < toLayer.length; ++to)
			{
				toLayer[to] += fromLayer[from] * connections[from][to];
			}
		}
	}

	public double[] tick(double[] input)
	{
		System.arraycopy(input, 0, allLayers[0], 0, input.length);
		//sigmoidArray(allLayers[0]);

		for(int i = 0; i < allLayers.length-1; i++)
		{
			singleStep(allLayers[i], connectionMatrices[i], allLayers[i+1]);
			sigmoidArray(allLayers[i+1]);
		}

		return allLayers[allLayers.length-1];
	}

	private static void sigmoidArray(double[] vals)
	{
		for(int i = 0; i < vals.length; i++)
		{
			vals[i] = sigmoid(vals[i]);
		}
	}

	private static double sigmoid(double x)
	{
		return Math.tanh(x);

		//1.0/(1.0 + e^(-16.0*x+8))
		//return 1.0 / (1.0 + Math.exp(-16.0 * x + 8));

		//(2.0 / (1.0 + e^(-5x))) - 1
		//return (2.0 / (1.0 + Math.exp(-8.0*x))) - 1;
	}

	public int getNumConnections()
	{
		return connections;
	}

	public double[][][] getAllConnections()
	{
		return connectionMatrices;
	}
}
