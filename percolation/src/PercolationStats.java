public class PercolationStats {
    private double _estPercolationThresholds [];
    private int _T;
    private double _mean;
    private double _stddev;

	private double conductExperiment(int N) {
		Percolation p = new Percolation(N);

		int count = 0;
		while (!p.percolates()) {
			int x = StdRandom.uniform(N*N);
			int i = x/N + 1;
			int j = x%N + 1;

			if(!p.isOpen(i, j))
			{
				p.open(i, j);
				count++;
			}
		}
		
		return (((double) count) / ((double) (N * N)));
	}

	public PercolationStats(int N, int T) // perform T independent computational
											// experiments on an N-by-N grid
	{
		if (N <= 0)
			throw new IllegalArgumentException(
					"Value of N cannot be less than or equal to 0 : N = " + N);
		if (T <= 0)
			throw new IllegalArgumentException(
					"Value of T cannot be less than or equal to 0 : T = " + T);
		
		_T = T;
		_estPercolationThresholds = new double[_T];
		
		for (int i = 0; i < T; i++) {
			_estPercolationThresholds[i] = conductExperiment(N);
		}
		
		_mean = StdStats.mean(_estPercolationThresholds);
		_stddev = StdStats.stddev(_estPercolationThresholds);

	}

	public double mean() // sample mean of percolation threshold
	{
		return _mean;
	}

	public double stddev() // sample standard deviation of percolation threshold
	{
		return _stddev;
	}

	public double confidenceLo() // returns lower bound of the 95% confidence interval
	{
		return _mean - (1.96*_stddev/Math.sqrt(_T));
	}

	public double confidenceHi() // returns upper bound of the 95% confidence interval
	{
		return _mean - (1.96*_stddev/Math.sqrt(_T));
	}

	public static void main(String[] args) // test client, described below
	{
		PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		System.out.println("mean                    = " + ps.mean());
		System.out.println("stddev                  = " + ps.stddev());
		System.out.println("95% confidence interval = " + ps.confidenceLo()
				+ ", " + ps.confidenceHi());
	}

}
