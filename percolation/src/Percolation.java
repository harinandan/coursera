/**
 * Percolation assignment.
 * @author harinandan
 *
 */
public class Percolation {
    private class Node {
        int data = -1;
        Node next = null;
    }

    private WeightedQuickUnionUF algorithm;
    private int N;
    private int NSQUARE;
    private boolean [][]grid;
    private Node first = null;
    private Node last = null;

    /** create N-by-N grid, with all sites blocked.
     *
     * @param N
     */
    public Percolation(int N) {
        this.N = N;
        NSQUARE = N * N;
        grid = new boolean[N][N];
        algorithm = new WeightedQuickUnionUF(N * N + 2);
        /*
        for (int q = 1; q <= N; q++) {
            algorithm.union(0, q);
        }
        int nSquare = N * N;
        for (int p = nSquare - N + 1; p <= nSquare; p++) {
            algorithm.union(p, nSquare + 1);
        }
        */
    }

    private void add(int d) {
        if (first == null) {
            first = new Node();
            last = first;
            first.data = d;
        } else {
            last.next = new Node();
            last.next.data = d;
            last = last.next;
        }
    }

    private void process() {
        Node prev = null;
        Node ret = first;
        while (ret != null) {
            if (algorithm.connected(0, ret.data)) {
                algorithm.union(ret.data, NSQUARE + 1);
                if (prev == null) {
                    first = ret.next;
                    if (first == null) {
                        last = null;
                    }
                } else {
                    prev.next = ret.next;
                    if (ret == last) {
                        last = prev;
                    }
                }
            }
            ret = ret.next;
            prev = ret;
        }
    }

    private boolean validIndex(int k) {
        if (k < 0 || k >= N) {
            return false;
        }

        return true;
    }

    private void validateSite(int i, int j) {
        if (!validIndex(i)) {
            throw new IndexOutOfBoundsException(
                "Value of i is out of bounds (1 <= i <= "
                        + N + ") : i = " + i);
        }
        if (!validIndex(j)) {
            throw new IndexOutOfBoundsException(
                "Value of j is out of bounds (1 <= j <= "
                        + N + ") : j = " + j);
        }
    }

    private int toOneDimensionIndex(int i, int j) {
        return i * N + j + 1;
    }

    /** open site (row i, column j) if it is not already.
     *
     * @param row
     * @param col
     */
    public void open(int i, int j) {
        int row = i - 1;
        int col = j - 1;
        validateSite(row, col);

        grid[row][col] = true;
        int x = toOneDimensionIndex(row, col);
        if (x >= 1 && x <= N) {
            algorithm.union(0, x);
        }
        if (x >= NSQUARE - N + 1 && x <= NSQUARE) {
            if (algorithm.connected(0, x)) {
                algorithm.union(x, NSQUARE + 1);
            } else {
                add(x);
            }
        }

        int p = toOneDimensionIndex(row, col);
        int q = -1;

        int k = row, l = col;

        if (validIndex(l)) {
            //Left
            k--;
            if (validIndex(k) && isOpen(k + 1, l + 1)) {
                q = toOneDimensionIndex(k, l);
                algorithm.union(p, q);
            }
            k++;

            //Right
            k++;
            if (validIndex(k) && isOpen(k + 1, l + 1)) {
                q = toOneDimensionIndex(k, l);
                algorithm.union(p, q);
            }
            k--;
        }

        if (validIndex(k)) {
            //Top
            l--;
            if (validIndex(l) && isOpen(k + 1, l + 1)) {
                q = toOneDimensionIndex(k, l);
                algorithm.union(p, q);
            }
            l++;

            //Bottom
            l++;
            if (validIndex(l) && isOpen(k + 1, l + 1)) {
                q = toOneDimensionIndex(k, l);
                algorithm.union(p, q);
            }
            l--;
        }

        process();
    }

    /** is site (row i, column j) open?
     * @param i
     * @param j
     * @return
     */
    public boolean isOpen(int i, int j) {
        int row = i - 1;
        int col = j - 1;
        validateSite(row, col);

        return grid[row][col];
    }

    /**  is site (row i, column j) full?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        int row = i - 1;
        int col = j - 1;
        validateSite(row, col);

        return isOpen(i, j)
            && algorithm.connected(0, toOneDimensionIndex(row, col));
    }

    /** does the system percolate?
     *
     * @return
     */
    public boolean percolates() {
        return algorithm.connected(0, NSQUARE + 1);
    }
}
