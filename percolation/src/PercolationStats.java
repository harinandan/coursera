public class PercolationStats {

    private static final double CONFIDENCECOEFF = 1.96;

    private double [] estPercolationThresholds;
    private int T;
    private double mean;
    private double stddev;

    /**
     * perform T independent computational experiments on an N-by-N grid.
     *
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N <= 0) {
            throw new IllegalArgumentException(
                    "Value of N cannot be less than or equal to 0 : N = " + N);
        }
        if (T <= 0) {
            throw new IllegalArgumentException(
                    "Value of T cannot be less than or equal to 0 : T = " + T);
        }

        this.T = T;
        estPercolationThresholds = new double[T];

        for (int i = 0; i < T; i++) {
            estPercolationThresholds[i] = conductExperiment(N);
        }

        mean = StdStats.mean(estPercolationThresholds);
        stddev = StdStats.stddev(estPercolationThresholds);

    }

    private double conductExperiment(int N) {
        Percolation p = new Percolation(N);

        int count = 0;
        while (!p.percolates()) {
            int i = StdRandom.uniform(N) + 1;
            int j = StdRandom.uniform(N) + 1;

            if (!p.isOpen(i, j)) {
                p.open(i, j);
                count++;
            }
        }

        return (((double) count) / ((double) (N * N)));
    }

    /** sample mean of percolation threshold.
     *
     * @return
     */
    public double mean() {
        return mean;
    }

    /** sample standard deviation of percolation threshold.
     *
     * @return
     */
    public double stddev() {
        return stddev;
    }

    /** returns lower bound of the 95% confidence interval.
     *
     * @return
     */
    public double confidenceLo() {
        return mean - (CONFIDENCECOEFF * stddev / Math.sqrt(T));
    }

    /** returns upper bound of the 95% confidence interval.
     *
     * @return
     */
    public double confidenceHi() {
        return mean - (CONFIDENCECOEFF * stddev / Math.sqrt(T));
    }

    /** test client, described below.
     *
     * @param args
     */
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo()
                + ", " + ps.confidenceHi());
    }

}
