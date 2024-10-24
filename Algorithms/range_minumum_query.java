import java.util.Scanner;

public class RangeMinimumQuery {

    // Square Root Decomposition
    static class SquareRootDecomposition {
        int[] arr;
        int[] blockMin;
        int blockWidth;

        public SquareRootDecomposition(int[] arr) {
            this.arr = arr;
            int n = arr.length;
            blockWidth = (int) Math.sqrt(n);
            blockMin = new int[n / blockWidth + 1];
            for (int i = 0; i < blockMin.length; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = i * blockWidth; j < Math.min((i + 1) * blockWidth, n); j++) {
                    min = Math.min(min, arr[j]);
                }
                blockMin[i] = min;
            }
        }

        public int query(int l, int r) {
            int leftBlock = l / blockWidth;
            int rightBlock = r / blockWidth;
            int min = Integer.MAX_VALUE;
            if (leftBlock == rightBlock) {
                for (int i = l; i <= r; i++) {
                    min = Math.min(min, arr[i]);
                }
            } else {
                for (int i = l; i < (leftBlock + 1) * blockWidth; i++) {
                    min = Math.min(min, arr[i]);
                }
                for (int i = rightBlock * blockWidth; i <= r; i++) {
                    min = Math.min(min, arr[i]);
                }
                for (int i = leftBlock + 1; i < rightBlock; i++) {
                    min = Math.min(min, blockMin[i]);
                }
            }
            return min;
        }
    }

    // Sparse Table
    static class SparseTable {
        int[][] lookup;
        int[] arr;

        public SparseTable(int[] arr) {
            this.arr = arr;
            int n = arr.length;
            lookup = new int[n][32];
            for (int i = 0; i < n; i++) {
                lookup[i][0] = i;
            }
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; (i + (1 << j) - 1) < n; i++) {
                    if (arr[lookup[i][j - 1]] < arr[lookup[i + (1 << (j - 1))][j - 1]]) {
                        lookup[i][j] = lookup[i][j - 1];
                    } else {
                        lookup[i][j] = lookup[i + (1 << (j - 1))][j - 1];
                    }
                }
            }
        }

        public int query(int l, int r) {
            int j = (int) (Math.log(r - l + 1) / Math.log(2));
            if (arr[lookup[l][j]] <= arr[lookup[r - (1 << j) + 1][j]]) {
                return arr[lookup[l][j]];
            } else {
                return arr[lookup[r - (1 << j) + 1][j]];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elements in the array: ");
        int n = scanner.nextInt();
        int[] arr = new int[n];

        System.out.println("Enter the elements of the array:");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Square Root Decomposition
        SquareRootDecomposition srd = new SquareRootDecomposition(arr);
        System.out.println("\nSquare Root Decomposition:");
        System.out.print("Enter the range (l r) for the query: ");
        int l = scanner.nextInt();
        int r = scanner.nextInt();
        System.out.println("Minimum of [" + l + ", " + r + "] is " + srd.query(l, r));

        // Sparse Table
        SparseTable st = new SparseTable(arr);
        System.out.println("\nSparse Table:");
        System.out.print("Enter the range (l r) for the query: ");
        l = scanner.nextInt();
        r = scanner.nextInt();
        System.out.println("Minimum of [" + l + ", " + r + "] is " + st.query(l, r));

        scanner.close();
    }
}