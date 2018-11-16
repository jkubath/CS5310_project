package final_project_cs5310_nearchou_kubath_1192018;


public class HullCreator {
	int[] pointSet = null;
	int[][] points = null;
	
	public void quickHull(int p, int q, int[] a) {
		
		
		
		if(a.length >= 2) {
			int j  = partition(p, q+1, a)
		
		}
	}
	
	public void hull() {
		
	}
	
	/**
	 * 
	 */
	private int determinant(int[][] a, int n) {
		
		if(n == 1) {
			
			return a[0][0];
			
		}else {
			
			
			//return determinant of matrix of 3-points
			return (a[0][0]*((a[1][1]*a[2][2]) - ((a[2][1]*a[1][2]))) -
					(a[0][1]*((a[1][0]*a[2][2]) - ((a[2][0]*a[1][2]))) + 
							(a[0][2]*((a[1][0]*a[2][1]) - ((a[2][0]*a[1][1])));
		}
		
	}
	
	/**
	 * This method is a partition implementation to be used by the quick sort
	 * implementation. The partition function is based on the algorithm provided in
	 * Computer Algorithms, 2nd Edition by Horowitz, Sahni and Rajasekeran. A pivot
	 * element is chosen as the variable v. Then, two indices are chosen to compare
	 * values from the left and right of the sub array to the pivot, in a loop. When
	 * the left index associates to an element with a value less than that of the
	 * pivot, the index is incremented. Just so, the right index is decremented when
	 * the element at that index is greater than the pivot. When the while loop
	 * executes in the next iteration, the comparisons begin at the next beginning
	 * index positions for i and j. After the inner comparison loops end, if i is
	 * less than j, a call to interchange is made, to swap those elements at those
	 * indices.
	 * 
	 * @param a integer array to be partitioned
	 * @param m left-most sub array index
	 * @param p right-most sub array index
	 * @return last partitioned index
	 */
	private int partition(int[] a, int m, int p) {

		int v = a[m]; // temporarily store value at mth element as the pivot
		int i = m; // set i to index m
		int j = p; // set j to index p

		// execute loop while i is less than j
		while (i < j) {

			// move index to first position of pivot left-hand comparisons
			i = i + 1;

			// loop and compare left-hand element values to the pivot
			while (a[i] < v) {

				i = i + 1; // increment index i

			}

			// move index over to first position of pivot right-hand comparisons
			j = j - 1;

			// loop and compare right-hand element values to the pivot
			while (a[j] > v) {

				j = j - 1; // decrement index j

			}

			// check if i is less than j and swap values at those element indices if so
			if (i < j) {

				interchange(a, i, j); // call interchange to swap values at elements i and j
			}

		}

		// swap values at pivot and last partition index
		a[m] = a[j];
		a[j] = v;

		return j; // return last partition index
	}
	
	/**
	 * This method performs a set union on a set of point vertices. The implementation
	 * of this optimized union variant is taken from the WeightedUnion algorithm given in
	 * Computer Algorithms, 2nd Edition by Horowitz, Sahni and Rajasekeran.
	 * @param i
	 * @param j
	 */
	public void union(int i, int j) {
		
		pointSet[i] = j;
		
	}

}
