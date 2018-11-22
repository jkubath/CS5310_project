package final_project_cs5310_nearchou_kubath_1192018;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RHullCreator {
	int[][] hull = null;

	/**
	 * This method is an implementation of quickhull.
	 * @param a array of 
	 */
	public void quickHull(int[][] a) {

		this.hull = new int[a.length][a[0].length];

		int n = a.length; // get number of points in point array
		int leftMost = 0, rightMost = 0; // declare indices for left most and right most points

		// store multiple points of left most x-coord value
		List<int[]> leftMostPoints = new LinkedList<int[]>();

		// store multiple points of right most x-coord value
		List<int[]> rightMostPoints = new LinkedList<int[]>();

		// list of points left of line segment for recursive subproblem breakdown
		List<int[]> x1 = new LinkedList<int[]>();

		// list of points right of line segment for recursive subproblem breakdown
		List<int[]> x2 = new LinkedList<int[]>();

		// loop to find left and right most extreme points in points array
		for (int i = 0; i < n; i++) {

			if (i == 0) {

				leftMost = i;
				rightMost = i;

			} else {

				// x-coord of point at index i is less than x-coord of current left most point,
				// set left most to i
				if (a[i][0] < a[leftMost][0]) {

					leftMostPoints.clear();
					leftMostPoints.add(a[i]);

					leftMost = i;

					// if tie between extreme left x-coord, add to list of leftmost points
				} else if (a[i][0] == a[leftMost][0]) {

					leftMostPoints.add(a[i]);
				}

				// x-coord of point at index i is greater than x-coord of current right most
				// point, set right most to i
				if (a[i][0] > a[rightMost][0]) {
					rightMostPoints.clear();
					rightMostPoints.add(a[i]);

					rightMost = i;

					// if tie between extreme left x-coord, add to list of rightmost points
				} else if (a[i][0] == a[rightMost][0]) {

					rightMostPoints.add(a[i]);

				}
			}
		}

		// check if no ties for right most and left most points
		if (rightMostPoints.size() == 1 && leftMostPoints.size() == 1) {

			for (int j = 0; j < a.length; j++) {

				// create 3x3 arrays to determine location of point at i relative to line seg of
				// left and rightmost
				int[][] detArr = new int[3][3];
				detArr[0][0] = leftMostPoints.get(0)[0];
				detArr[0][1] = leftMostPoints.get(0)[1];
				detArr[0][2] = 1;
				detArr[1][0] = rightMostPoints.get(0)[0];
				detArr[1][1] = rightMostPoints.get(0)[1];
				detArr[1][2] = 1;
				detArr[2][0] = a[j][0];
				detArr[2][1] = a[j][1];
				detArr[2][2] = 1;

				// check if point at points[i] is to the left of line seg of leftmost and
				// rightmost
				if (determinant(a, 3) > 0) {
					interchange(a, leftMost, j);
				}

			}

			//handle ties here
		} else {

			int[] p1 = leftMostPoints.get(0);
			int[] p11 = leftMostPoints.get(0);
			int[] p2 = rightMostPoints.get(0);
			int[] p22 = rightMostPoints.get(0);
			
			//loop through each tied extreme left most point in a
			for(int i = 0; i < leftMostPoints.size(); i++) {
				
				//if y-coord of current leftmost point is less than current extreme, set it to that point
				if(p1[1] > leftMostPoints.get(i)[1]) {
					
					p1 = leftMostPoints.get(i);
				}
				
				//if y-coord of current leftmost point is greater than current extreme, set it to that point
				if(p11[1] < leftMostPoints.get(i)[1]) {
					
					p11 = leftMostPoints.get(i);
				}
				
				
			}
			
			//loop through each tied extreme right most point in a
			for(int i = 0; i < rightMostPoints.size(); i++) {
				
				
				//if y-coord of current rightmost point is less than current extreme, set it to that point
				if(p2[1] > rightMostPoints.get(i)[1]) {
					
					p2 = rightMostPoints.get(i);
				}
				
				//if y-coord of current rightmost point is greater than current extreme, set it to that point
				if(p22[1] < rightMostPoints.get(i)[1]) {
					
					p22 = rightMostPoints.get(i);
				}
			}
			

		}
		
		rHull(leftMost, rightMost, a);
		rHull(rightMost, leftMost, a);

	}

	

	/**
	 * 
	 * @param p
	 * @param q
	 * @param a
	 */
	private void rHull(int p, int q, int[][] a) {

		Random rand = new Random();

		if (p < q) {

			if ((q - p) > 5) {

				interchange(a, rand.nextInt() % (q - p), p);
			}

			int j = partition(a, p, q + 1);

			rHull(p, j - 1, a);
			rHull(j + 1, q, a);

		}
	}

	/**
	 * 
	 * @param a
	 * @param n
	 * @return
	 */
	private int determinant(int[][] a, int n) {

		if (n == 1) {

			return a[0][0];

		} else {

			// return determinant of matrix of 3-points
			return (a[0][0] * ((a[1][1] * a[2][2]) - (a[2][1] * a[1][2])))
					- (a[0][1] * ((a[1][0] * a[2][2]) - (a[2][0] * a[1][2])))
					+ (a[0][2] * ((a[1][0] * a[2][1]) - (a[2][0] * a[1][1])));
		}

	}
	
	/**
	 * This method is a modified partition implementation to be used by the hull
	 * implementation. The partition function is based on the algorithm provided in
	 * Computer Algorithms, 2nd Edition by Horowitz, Sahni and Rajasekeran. A pivot
	 * element is chosen as the variable v. Then, two indices are chosen to compare
	 * values from the left and right of the sub array to the pivot, in a loop. When
	 * the left index associates to an element with a smaller x-coord than that of the
	 * pivot, the index is incremented. Just so, the right index is decremented when
	 * the element at that index x-coord is greater than the pivot. When the while loop
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
	private int partition(int[][] a, int m, int p) {

		int[] v = a[m]; // temporarily store value at mth element as the pivot
		int i = m; // set i to index m
		int j = p; // set j to index p

		// execute loop while i is less than j
		while (i < j) {

			// move index to first position of pivot left-hand comparisons
			i = i + 1;

			// loop and compare left-hand element values to the pivot
			while (a[i][0] < v[0]) {

				i = i + 1; // increment index i

			}

			// move index over to first position of pivot right-hand comparisons
			j = j - 1;

			// loop and compare right-hand element values to the pivot
			while (a[j][0] > v[0]) {

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
	 * 
	 * @param a
	 * @param i
	 * @param j
	 */
	private void interchange(int[][] a, int i, int j) {

		int[] temp = a[i];
		a[i] = a[j];
		a[j] = temp;

	}

	/**
	 * @return the hull
	 */
	public int[][] getHull() {
		return hull;
	}

	/**
	 * @param hull the hull to set
	 */
	public void setHull(int[][] hull) {
		this.hull = hull;
	}

}
