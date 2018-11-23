package final_project_cs5310_nearchou_kubath_1192018;

import java.util.LinkedList;
import java.util.List;

public class DPHullCreator {
	

	/**
	 * This method is an implementation of quickhull.
	 * 
	 * @param a array of
	 * @return
	 */
	public int[][] quickHull(int[][] a) {

		// if array of vertices is null or not greater than two, return the array as the
		// hull
		if (a == null || a.length <= 2) {

			return a;
		}

		int[][] hull = null;

		int n = a.length; // get number of points in point array
		int leftMost = 0, rightMost = 0; // declare indices for left most and right most points

		// store multiple points of left most x-coord value
		List<int[]> leftMostPoints = new LinkedList<int[]>();

		// store multiple points of right most x-coord value
		List<int[]> rightMostPoints = new LinkedList<int[]>();

		// list of points left of line segment for recursive subproblem breakdown
		List<int[]> x1List = new LinkedList<int[]>();

		// list of points right of line segment for recursive subproblem breakdown
		List<int[]> x2List = new LinkedList<int[]>();

		int[][] x1 = null;

		int[][] x2 = null;

		int[][] upperHull = null;
		int[][] lowerHull = null;

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

			for (int i = 0; i < a.length; i++) {

				// create 3x3 arrays to determine location of point at i relative to line seg of
				// left and rightmost
				int[][] detArr = new int[3][3];
				detArr[0][0] = leftMostPoints.get(0)[0];
				detArr[0][1] = leftMostPoints.get(0)[1];
				detArr[0][2] = 1;
				detArr[1][0] = rightMostPoints.get(0)[0];
				detArr[1][1] = rightMostPoints.get(0)[1];
				detArr[1][2] = 1;
				detArr[2][0] = a[i][0];
				detArr[2][1] = a[i][1];
				detArr[2][2] = 1;

				// check if point at points[i] is to the left of line seg of leftmost and
				// rightmost
				if (determinant(a, 3) > 0) {
					x1List.add(a[i]);
				}

				if (determinant(a, 3) < 0) {
					x2List.add(a[i]);
				}

			}

			x1 = new int[x1List.size() + 2][leftMostPoints.get(0).length];

			x2 = new int[x2List.size() + 2][rightMostPoints.get(0).length];

			x1[0] = a[leftMost];
			x1[x1.length - 1] = a[rightMost];

			x2[0] = a[rightMost];
			x2[x2.length - 1] = a[leftMost];

			upperHull = dPHull(0, x1.length - 1, x1);
			lowerHull = dPHull(0, x2.length - 1, x2);

			// handle ties here
		} else {

			int[] p1 = leftMostPoints.get(0);
			int[] p11 = leftMostPoints.get(0);
			int[] p2 = rightMostPoints.get(0);
			int[] p22 = rightMostPoints.get(0);
			int r1 = 0;
			int r2 = 0;
			int l1 = 0;
			int l2 = 0;

			// loop through each tied extreme left most point in a
			for (int i = 0; i < leftMostPoints.size(); i++) {

				// if y-coord of current leftmost point is less than current extreme, set it to
				// that point
				if (p1[1] > leftMostPoints.get(i)[1]) {

					p1 = leftMostPoints.get(i);
					l1 = i;
				}

				// if y-coord of current leftmost point is greater than current extreme, set it
				// to that point
				if (p11[1] < leftMostPoints.get(i)[1]) {

					p11 = leftMostPoints.get(i);
					l2 = i;
				}

			}

			// loop through each tied extreme right most point in a
			for (int i = 0; i < rightMostPoints.size(); i++) {

				// if y-coord of current rightmost point is less than current extreme, set it to
				// that point
				if (p2[1] > rightMostPoints.get(i)[1]) {

					p2 = rightMostPoints.get(i);
					r1 = i;
				}

				// if y-coord of current rightmost point is greater than current extreme, set it
				// to that point
				if (p22[1] < rightMostPoints.get(i)[1]) {

					p22 = rightMostPoints.get(i);
					r2 = i;
				}
			}

			x1 = new int[x1List.size() + 2][leftMostPoints.get(0).length];

			x2 = new int[x2List.size() + 2][rightMostPoints.get(0).length];

			x1[0] = p1;
			x1[x1.length - 1] = p11;

			x2[0] = p2;
			x2[x2.length - 1] = p22;

			// perform divide step of DCG to get hulls of sub arrays
			upperHull = dPHull(l2, r2, a);
			lowerHull = dPHull(l1, r1, a);

		}

		// set hull size for glue step of DCG for convex hull problem
		if (upperHull == null) {

			hull = lowerHull;

		} else if (lowerHull == null) {

			hull = upperHull;

		} else {

			hull = new int[upperHull.length + lowerHull.length][upperHull[0].length];

			// declare and initialize hull index for merging returned upper and lower hulls
			// to hull
			int hullIndex = 0;

			// merge upper hull
			for (int i = 0; i < upperHull.length; i++) {

				hull[hullIndex++] = upperHull[i];

			}

			// merge lower hull
			for (int i = 0; i < lowerHull.length; i++) {

				hull[hullIndex++] = lowerHull[i];
			}

		}

		// return hull
		return hull;

	}

	/**
	 * This method is the recursive quicksort-like method that breaks the problem of
	 * finding points of a 2-d graph to form a convex hull. This implementation is
	 * based on qualitative directions given by Computer Algorithms, 2nd Edition by
	 * HSR. Its name is attributed to the kind of partition function it calls, which
	 * is implemented with a dual-pivot strategy for optimized partitioning of the
	 * current array into sub arrays.
	 * 
	 * @param p
	 * @param q
	 * @param a
	 */
	private int[][] dPHull(int p, int q, int[][] a) {

		// if the span of the sub array is greater than two points, compute hull
		if ((q - p) > 2) {

			// partition array and sort points by x-coordinate
			partition(a, p, q);

			// declare variable to store a point to
			int[] hullPoint = a[p];
			int hullPointIndex = p;

			int maxArea = 0;

			int[][] detArr = null;

			// find the point in the sub array, where the there is a maximum triangular area
			// with p and q
			for (int i = p + 1; i < q; i++) {

				// create 3x3 arrays to determine location of point at i relative to line seg of
				// left and rightmost
				detArr = new int[3][3];
				detArr[0][0] = a[p][0];
				detArr[0][1] = a[p][1];
				detArr[0][2] = 1;
				detArr[1][0] = a[q][0];
				detArr[1][1] = a[q][1];
				detArr[1][2] = 1;
				detArr[2][0] = a[i][0];
				detArr[2][1] = a[i][1];
				detArr[2][2] = 1;

				// check if current maximum triangle area is less than that of the triangle
				// formed by p,q,i
				if (maxArea < (determinant(detArr, 3) / 2)) {

					// set point
					hullPoint = a[i];
					hullPointIndex = i;
				}

			}

			// recursively breakdown problem to upper and lower hulls of left and right sub
			// arrays
			int[][] upperHull = dPHull(p, hullPointIndex, a);
			int[][] lowerHull = dPHull(hullPointIndex, q, a);

			int[][] hull = null; // declare variable to hold the hull

			// if both upper and lower hulls are null, set hull of this sub problem to the
			// partition point
			if (upperHull == null && lowerHull == null) {

				hull = new int[1][hullPoint.length];
				hull[0] = hullPoint;

			} else // set hull size for glue step of DCG for convex hull problem by partition point
					// and lower hull
			if (upperHull == null) {

				hull = new int[lowerHull.length + 1][lowerHull[0].length];

				for (int i = 0; i < lowerHull.length; i++) {
					hull[i] = lowerHull[i];
				}

				hull[hull.length - 1] = hullPoint;

			} else if (lowerHull == null) {

				hull = new int[upperHull.length + 1][upperHull[0].length];

				hull[0] = hullPoint;

				for (int i = 0; i < upperHull.length; i++) {
					hull[i + 1] = upperHull[i];
				}

			} else {

				hull = new int[upperHull.length + lowerHull.length + 1][upperHull[0].length];

				// declare and initialize hull index for merging returned upper and lower hulls
				// to hull
				int hullIndex = 0;

				// merge upper hull
				for (int i = 0; i < upperHull.length; i++) {

					hull[hullIndex++] = upperHull[i];

				}

				// merge lower hull
				for (int i = 0; i < lowerHull.length; i++) {

					hull[hullIndex++] = lowerHull[i];
				}

			}

			// return hull
			return hull;

		}

		return null;
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
	 * Algorithms, 4th Edition by Sedgewick and Wayne. This is a dual pivot variant,
	 * where two pivots are chosen, one at index m and the other at p. Then, three
	 * indices are chosen to compare and swap values from the left and right of the
	 * array to the pivots, in a loop. When the left index associates to an element
	 * with a smaller x-coord than that of the left pivot, the index is incremented.
	 * Just so, the right index is decremented when the element at that index
	 * x-coord is greater than the right pivot. If neither are the case, only the i
	 * 
	 * @param a integer array to be partitioned
	 * @param m left-most sub array index
	 * @param p right-most sub array index
	 * @return last partitioned index
	 */
	private int partition(int[][] a, int m, int p) {

		int[] leftPiv = a[m]; // choose element at m as left pivot
		int[] rightPiv = a[p]; // choose element at p as right pivot

		int left = m + 1; // set left to index m
		int right = p - 1; // set right to index p

		int i = left + 1;

		// execute loop while i is less than or equal to right
		while (i <= right) {

			// compare current element is to the left of the left pivot
			if (a[i][0] < leftPiv[0]) {

				interchange(a, i, left); // call interchange to swap points at elements i and left
				left++; // increment left
				i++; // increment i

				// compare if current element is to the right of right pivot
			} else if (a[i][0] > rightPiv[0]) {

				interchange(a, i, right); // call interchange to swap points at elements i and right
				right--; // decrement right
				i++; // increment i

			} else {

				i++; // increment i

			}

		}

		// swap points between left and m, and right and p
		interchange(a, m, left - 1);
		interchange(a, p, right + 1);

		// if left is less than right, set the partition value to the middle index
		// between them
		if (left < right) {

			i = ((left + right) / 2);
		}

		return i; // return last partition index
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

}
