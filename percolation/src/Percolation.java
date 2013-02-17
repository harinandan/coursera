public class Percolation {
	private WeightedQuickUnionUF _algorithm;
	private int _N;
	private boolean _grid[][]; 

	private boolean validIndex(int k)
	{
		if(k < 0 || k >= _N)
			return false;
		
		return true;
	}
	
	private int toOneDimensionIndex(int i, int j)
	{
		return i*_N + j;
	}
	
	public Percolation(int N) // create N-by-N grid, with all sites blocked
	{
		_N = N;
		_grid = new boolean[_N][_N];
		_algorithm = new WeightedQuickUnionUF(_N*_N);
	}

	public void open(int i, int j) // open site (row i, column j) if it is not already
	{
		if(!validIndex(i-1))
			throw new IndexOutOfBoundsException("Value of i is out of bounds (1 <= i <= "+_N+") : i = "+i); 
		if(!validIndex(j-1))
			throw new IndexOutOfBoundsException("Value of j is out of bounds (1 <= j <= "+_N+") : j = "+j);
		i--;
		j--;

		_grid[i][j] = true;
		
		int p = toOneDimensionIndex(i, j);
		int q = -1;
		
		int k = i, l = j;
		
		if(validIndex(l))
		{
			//Left
			k--;
			if(validIndex(k) && isOpen(k+1,l+1))
			{
				q = toOneDimensionIndex(k, l);
				_algorithm.union(p, q);
			}
			k++;
			
			//Right
			k++;
			if(validIndex(k) && isOpen(k+1,l+1))
			{
				q = toOneDimensionIndex(k, l);
				_algorithm.union(p, q);
			}
			k--;
		}
		
		if(validIndex(k))
		{
			//Top
			l--;
			if(validIndex(l) && isOpen(k+1,l+1))
			{
				q = toOneDimensionIndex(k, l);
				_algorithm.union(p, q);
			}
			l++;
			
			//Bottom
			l++;
			if(validIndex(l) && isOpen(k+1,l+1))
			{
				q = toOneDimensionIndex(k, l);
				_algorithm.union(p, q);
			}
			l--;
		}
		
	}

	public boolean isOpen(int i, int j) // is site (row i, column j) open?
	{
		if(!validIndex(i-1))
			throw new IndexOutOfBoundsException("Value of i is out of bounds (1 <= i <= "+_N+") : i = "+i); 
		if(!validIndex(j-1))
			throw new IndexOutOfBoundsException("Value of j is out of bounds (1 <= j <= "+_N+") : j = "+j);
		i--;
		j--;

		return _grid[i][j];
	}

	public boolean isFull(int i, int j) // is site (row i, column j) full?
	{
		if(!validIndex(i-1))
			throw new IndexOutOfBoundsException("Value of i is out of bounds (1 <= i <= "+_N+") : i = "+i); 
		if(!validIndex(j-1))
			throw new IndexOutOfBoundsException("Value of j is out of bounds (1 <= j <= "+_N+") : j = "+j);
		i--;
		j--;
		
		boolean full = false;
		for (int p = 0; p < _N && !full; p++) {
			full = _algorithm.connected(p, toOneDimensionIndex(i, j));
		}
		return full;
	}

	public boolean percolates() // does the system percolate?
	{
		boolean percolates = false;
		for (int j = 1; j <= _N && !percolates; j++) {
			percolates = isFull(_N, j);
		}
		return percolates;
	}
	
	public static void main(String[] args) {
		int N = 4;
		Percolation p = new Percolation(N);
		
		for (int i = 1; i <= N; i++) 
		{
			for (int j = 1; j <= N; j++)
			{
				boolean open = p.isOpen(i, j);
				assert !open : "isOpen() returned incorrect state for i="+i +", j="+ j;
			}
		}
		
		p.open(1, 2);
		p.open(2, 2);
		p.open(2, 3);
		p.open(3, 3);
		p.open(3, 4);
		p.open(4, 4);
		
		System.out.println(p.percolates());
	}
}
