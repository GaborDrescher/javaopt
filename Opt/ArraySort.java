import java.util.Comparator;

public final class ArraySort<T>
{
	private T[] array;
	private Comparator<? super T> comp;
	private final int[] stack;

	public ArraySort(Comparator<? super T> comp)
	{
		this.comp = comp;
		stack = new int[128];
	}

	public void setComparator(Comparator<? super T> comp)
	{
		this.comp = comp;
	}

	public void quicksort(T[] array)
	{
		this.array = array;
		//rekquicksort(0, array.length-1);
		iterquicksort(0, array.length-1);
	}

	private void rekquicksort(final int begin, final int end)
	{
		if(end - begin < 6)
		{
			//insertion sort
			for (int i = begin; i <= end; ++i)
			{
                for (int k = i; (k > begin) && (comp.compare(array[k-1], array[k]) > 0); --k)
				{
					final T tmp = array[k];
					array[k] = array[k-1];
					array[k-1] = tmp;
				}
			}

            return;
		}

		final int mid = (begin >> 1) + (end >> 1);

		//median of three
		if(comp.compare(array[begin], array[mid]) > 0)
		{
			final T tmp = array[begin];
			array[begin] = array[mid];
			array[mid] = tmp;
		}
		if(comp.compare(array[begin], array[end]) > 0)
		{
			final T tmp = array[begin];
			array[begin] = array[end];
			array[end] = tmp;
		}
		if(comp.compare(array[mid], array[end]) > 0)
		{
			final T tmp = array[mid];
			array[mid] = array[end];
			array[end] = tmp;
		}
		T tmp = array[mid];
		array[mid] = array[end];
		array[end] = tmp;
		//

		final T pivot = array[end];

		int i = begin - 1;
		for(int j = begin; j < end; ++j)
		{
			if(comp.compare(array[j], pivot) <= 0)
			{
				++i;
				tmp = array[j];
				array[j] = array[i];
				array[i] = tmp;
			}
		}

		tmp = array[i+1];
		array[i+1] = array[end];
		array[end] = tmp;

		final int pivotpos = i+1;

		if(pivotpos - begin < pivotpos - end)
		{
			rekquicksort(begin, pivotpos-1);
			rekquicksort(pivotpos+1, end);
		}
		else
		{
			rekquicksort(pivotpos+1, end);
			rekquicksort(begin, pivotpos-1);
		}
	}

	///* somehow slower than the recursive version
	private void iterquicksort(final int b, final int e)
	{
		int stackpos = 0;
		stack[stackpos++] = b;
		stack[stackpos++] = e;

		while(stackpos != 0)
		{
			final int end = stack[--stackpos];
			final int begin = stack[--stackpos];

			if(end - begin < 6)
			{
				//insertion sort
				for(int i = begin; i <= end; ++i)
				{
					for(int k = i; (k > begin) && (comp.compare(array[k - 1], array[k]) > 0); --k)
					{
						final T tmp = array[k];
						array[k] = array[k - 1];
						array[k - 1] = tmp;
					}
				}

				continue;
			}

			final int mid = (begin >> 1) + (end >> 1);

			//median of three
			if(comp.compare(array[begin], array[mid]) > 0)
			{
				final T tmp = array[begin];
				array[begin] = array[mid];
				array[mid] = tmp;
			}
			if(comp.compare(array[begin], array[end]) > 0)
			{
				final T tmp = array[begin];
				array[begin] = array[end];
				array[end] = tmp;
			}
			if(comp.compare(array[mid], array[end]) > 0)
			{
				final T tmp = array[mid];
				array[mid] = array[end];
				array[end] = tmp;
			}
			//
			T tmp = array[mid];
			array[mid] = array[end];
			array[end] = tmp;

			final T pivot = array[end];

			int i = begin - 1;
			for(int j = begin; j < end; ++j)
			{
				if(comp.compare(array[j], pivot) <= 0)
				{
					++i;
					tmp = array[j];
					array[j] = array[i];
					array[i] = tmp;
				}
			}

			tmp = array[i + 1];
			array[i + 1] = array[end];
			array[end] = tmp;

			final int pivotpos = i + 1;

			if(pivotpos - begin < pivotpos - end)
			{
				stack[stackpos++] = begin;
				stack[stackpos++] = pivotpos - 1;

				stack[stackpos++] = pivotpos + 1;
				stack[stackpos++] = end;
			}
			else
			{
				stack[stackpos++] = pivotpos + 1;
				stack[stackpos++] = end;

				stack[stackpos++] = begin;
				stack[stackpos++] = pivotpos - 1;
			}
		}
	}
	//*/
}