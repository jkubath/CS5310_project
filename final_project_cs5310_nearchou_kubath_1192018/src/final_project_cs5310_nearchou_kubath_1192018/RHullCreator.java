package final_project_cs5310_nearchou_kubath_1192018;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RHullCreator {
	private List<int[]> hullList = null; // dynamic list for maintaining record of hull points with each hull call
	private List<int[]> subListLeft = null; //dynamic list for breaking down first half of hull subproblem
	private List<int[]> subListRight = null; //dynamic list for breaking down second half of hull subproblem


	/**
	 * This method is an implementation of quick hull. It is used to compute a
	 * polygon that surrounds a space of points on a 2-d graph, through the outer
	 * most points of that graph. The implementation is taken from directions given
	 * in the textbook Computer Algorithms, 2nd Edition by Horowitz, Sahni and
	 * Rajasekeran. This method takes a 2-d array representing an array of graph
	 * point coordinates. It then finds the extreme points in the list by largest
	 * and smallest x-coordinate values and forms a line segment from them. Then,
	 * each other point in the graph is compared to see if it is oriented to the
	 * right or left of the segment. These points are placed into lists that will be
	 * used to create arrays with points to the left and right of the line segment,
	 * with the line segment points included in each array. Then, the local hull()
	 * method implementation is called to recursively breakdown the list into sub
	 * problems and find points that will belong to the hull of the graph. If there
	 * is a tie between extreme points, all points of tied lowest x-value and
	 * highest x-value are compared to see which two in each set have the highest
	 * and lowest y-values, which are used as extremes for those lists. Then, the
	 * subproblem arrays are defined by the left and right extremes with largest
	 * y-values forming a line segment and the left and right extremes with smallest
	 * y-values forming the other. After all calls of the recursive hull method end,
	 * the resulting points of the hull from each subproblem are added to the hull
	 * array that is returned by this method.
	 * 
	 * @param a array of point coordinates of a of a 2-d graph
	 * @return hull of the a
	 */
	public int[][] quickHull(int[][] a) {

		// if array of vertices is null or not greater than two, return the array as the
		// hull
		if (a == null || a.length <= 2) {

			return a;
		}

		int[][] hull = null;

		this.hullList = new ArrayList<int[]>(); // initialize dynamic list to hold hull points from subproblems

		int n = a.length; // get number of points in point array
		int leftMost = 0, rightMost = 0; // declare indices for left most and right most points

		// store multiple points of left most x-coord value
		List<int[]> leftMostPoints = new ArrayList<int[]>();

		// store multiple points of right most x-coord value
		List<int[]> rightMostPoints = new ArrayList<int[]>();

		// list of points left of line segment for recursive subproblem breakdown
		List<int[]> x1List = new ArrayList<int[]>();

		// list of points right of line segment for recursive subproblem breakdown
		List<int[]> x2List = new ArrayList<int[]>();

		int[][] x1 = null; // declare reference to first sub array of sub problem divide step

		int[][] x2 = null; // declare reference to second sub array of sub problem divide step

		leftMostPoints.add(a[leftMost]);
		rightMostPoints.add(a[rightMost]);
		
		// loop to find left and right most extreme points in points array
		for (int i = 1; i < n; i++) {

			// x-coord of point at index i is less than x-coord of current left most point,
			// set left most to i
			if (a[i][1] < a[leftMost][1]) {

				leftMostPoints.clear();
				leftMostPoints.add(a[i]);

				leftMost = i;

				// if tie between extreme left x-coord, add to list of leftmost points
			} else if (a[i][1] == a[leftMost][1]) {

				leftMostPoints.add(a[i]);
			}

			// x-coord of point at index i is greater than x-coord of current right most
			// point, set right most to i
			if (a[i][1] > a[rightMost][1]) {
				rightMostPoints.clear();
				rightMostPoints.add(a[i]);

				rightMost = i;

				// if tie between extreme left x-coord, add to list of rightmost points
			} else if (a[i][1] == a[rightMost][1]) {

				rightMostPoints.add(a[i]);

			}
		}

		// check if no ties for right most and left most points
		if (rightMostPoints.size() == 1 && leftMostPoints.size() == 1) {

			for (int i = 0; i < a.length; i++) {

				// check if point at points[i] is to the left of line seg of leftmost and
				// rightmost
				if (determinant(leftMostPoints.get(0), rightMostPoints.get(0), a[i]) > 0) {
					x1List.add(a[i]);
				}

				if (determinant(leftMostPoints.get(0), rightMostPoints.get(0), a[i]) < 0) {
					x2List.add(a[i]);
				}

			}

			// create arrays for sub arrays X1 and X2 to breakdown recursively to solve sub
			// problems
			x1 = new int[x1List.size() + 2][leftMostPoints.get(0).length];

			x2 = new int[x2List.size() + 2][rightMostPoints.get(0).length];

			x1[0] = a[leftMost];
			x1[x1.length - 1] = a[rightMost];

			for (int i = 0; i < x1List.size(); i++) {
				x1[i + 1] = x1List.get(i);
			}

			x2[0] = a[rightMost];
			x2[x2.length - 1] = a[leftMost];

			for (int i = 0; i < x2List.size(); i++) {
				x2[i + 1] = x2List.get(i);
			}

			// add endpoints of line segment to hull list and call rhull to put in the
			// others
			this.hullList.add(a[leftMost]);

			rHull(0, x1.length - 1, x1);
			rHull(0, x2.length - 1, x2);

			this.hullList.add(a[rightMost]);

			// handle ties here
		} else {

			int[] p1 = leftMostPoints.get(0); // left point of smallest y-coord value
			int[] p11 = leftMostPoints.get(0); // left point of largest y-value
			int[] p2 = rightMostPoints.get(0); // right point of smallest y-value
			int[] p22 = rightMostPoints.get(0); // right point of largest y-value

			if (leftMostPoints.size() > 1) {
				// loop through each tied extreme left most point in a
				for (int i = 0; i < leftMostPoints.size(); i++) {

					// if y-coord of current leftmost point is less than current extreme, set it to
					// that point
					if (p1[0] > leftMostPoints.get(i)[0]) {

						p1 = leftMostPoints.get(i);

					}

					// if y-coord of current leftmost point is greater than current extreme, set it
					// to that point
					if (p11[0] < leftMostPoints.get(i)[0]) {

						p11 = leftMostPoints.get(i);

					}

				}

				p1 = leftMostPoints.get(0);
				p11 = leftMostPoints.get(leftMostPoints.size() - 1);
			} else {

				p1 = p11 = leftMostPoints.get(0);
			}

			if (rightMostPoints.size() > 1) {
				// loop through each tied extreme right most point in a
				for (int i = 0; i < rightMostPoints.size(); i++) {

					// if y-coord of current rightmost point is less than current extreme, set it to
					// that point
					if (p2[1] > rightMostPoints.get(i)[1]) {

						p2 = rightMostPoints.get(i);

					}

					// if y-coord of current rightmost point is greater than current extreme, set it
					// to that point
					if (p22[0] < rightMostPoints.get(i)[0]) {

						p22 = rightMostPoints.get(i);

					}
				}

				p2 = rightMostPoints.get(0);
				p22 = rightMostPoints.get(rightMostPoints.size() - 1);

			} else {
				p2 = p22 = rightMostPoints.get(0);
			}

			for (int i = 0; i < a.length; i++) {

				

				// check if point at points[i] is to the left of line seg of p11 and
				// p22
				if (determinant(p11, p22, a[i]) > 0) {
					x1List.add(a[i]);
				}

				// check if point at points[i] is to the right of line seg of p1 and
				// p2
				if (determinant(p1, p2, a[i]) < 0) {
					x2List.add(a[i]);
				}

			}

			// create arrays for sub arrays X1 and X2 to breakdown recursively to solve sub
			// problems
			x1 = new int[x1List.size() + 2][p11.length];

			x2 = new int[x2List.size() + 2][p1.length];

			x1[0] = p11;
			x1[x1.length - 1] = p22;

			for (int i = 0; i < x1List.size(); i++) {
				x1[i + 1] = x1List.get(i);
			}

			x2[0] = p1;
			x2[x2.length - 1] = p2;

			for (int i = 0; i < x2List.size(); i++) {
				x2[i + 1] = x2List.get(i);
			}

			// add line segment endpoints to overall hull list
			// add the line segment points to the hull

			this.hullList.add(p1);
			if (p1 != p11) {
				this.hullList.add(p11);
			}

			this.hullList.add(p2);

			if (p2 != p22) {
				this.hullList.add(p22);
			}

			// perform divide step of DCG to get hulls of sub arrays
			rHull(0, x1.length - 1, x1);
			rHull(0, x2.length - 1, x2);

		}

		// if dynamic list containing hull points is not empty, add hull points to the
		// hull to be returned
		if (this.hullList.size() > 0) {

			hull = new int[this.hullList.size()][this.hullList.get(0).length];

			// set hull points from dynamic list kept during hull calls
			for (int i = 0; i < this.hullList.size(); i++) {

				hull[i] = this.hullList.get(i);
			}

		}

		// return hull
		return hull;

	}

	/**
	 * This method is a modified hull that is inspired by RSort from HSR. A
	 * randomized point swap is done before calculating the partition index to
	 * subdivide the array a with recursive calls to find hull points between line
	 * segments formed by point p and the partition and point q and the partition.
	 * 
	 * @param p lowest index of the sub array
	 * @param q highest index of the sub array
	 * @param a array to be broken down and partitioned by a recursive hull call
	 */
	private void rHull(int p, int q, int[][] a) {

		// check if the number of elements in sub array is greater than 2
		if ((q - p + 1) > 2) {

			Random rand = new Random();

			// choose random point in sub array between p and q to swap with point at p + 1
			if ((q - p) > 5) {

				interchange(a, rand.nextInt((q - p - 2)) + 1, p + 1);
			}

			// partition sub array and sort points by x-coordinate, scope between p and q
			// since the extreme points must maintain their positions
			int hullPointIndex = partition(a, p, q);

			// add partition point to the dynamic hull point list
			this.hullList.add(a[hullPointIndex]);

			//create arrays for subproblems and set with their points
			int[][] x1 = new int[this.subListLeft.size()][];
			int[][] x2 = new int[this.subListRight.size()][];

			for (int i = 0; i < this.subListLeft.size(); i++) {
				x1[i] = this.subListLeft.get(i);
			}

			for (int i = 0; i < this.subListRight.size(); i++) {
				x2[i] = this.subListRight.get(i);
			}

			this.subListLeft.clear();
			this.subListRight.clear();

			// recursively breakdown problem to upper and lower hulls of left and right sub
			// arrays
			rHull(0, x1.length - 1, x1);
			rHull(0, x2.length - 1, x2);

		}

	}

	/**
	 * This method is used to calculate the determinant of a matrix, based on the
	 * implementation given in cse.unl.edu/~ylu/raik283/notes/quickhull.ppt. In
	 * context to the quick hull problem, the assumption is made that the matrix is
	 * made from 3 points p, q and r, due to this method being required for
	 * calculating the angle of a given point r in a graph, to a line segment formed
	 * by two other points. TheIt is noted that from the HSR textbook, that the
	 * determinant of a matrix that represents an array of points can refer to the
	 * orientation of r to the line segment (left or right) by the sign of the
	 * determinant and that the magnitude is the angle.
	 * 
	 * @param p  left line segment endpoint
	 * @param q  right line segment endpoint
	 * @param r point to be compared to the line segment
	 * @return the signed determinant result of matrix a
	 */
	private int determinant(int[] p, int[] q, int[] r) {

		// create 3x3 arrays to determine location of point at r relative to line seg of
		// p and q
		int[][] a = new int[3][3];
		a[0][0] = p[1];
		a[0][1] = p[0];
		a[0][2] = 1;
		a[1][0] = q[1];
		a[1][1] = q[0];
		a[1][2] = 1;
		a[2][0] = r[1];
		a[2][1] = r[0];
		a[2][2] = 1;

		// return determinant of matrix of 3-points
		return (a[0][0] * ((a[1][1] * a[2][2]) - (a[2][1] * a[1][2])))
				- (a[0][1] * ((a[1][0] * a[2][2]) - (a[2][0] * a[1][2])))
				+ (a[0][2] * ((a[1][0] * a[2][1]) - (a[2][0] * a[1][1])));

	}

	/**
	 * This method is a modified partition implementation to be used by the hull
	 * implementation. The partition function is based on the algorithm provided in
	 * Computer Algorithms, 2nd Edition by Horowitz, Sahni and Rajasekeran, but
	 * modified to find the proper pivot for a quick hull sub problem breakdown. A
	 * pivot element is first chosen as the variable v. Then, two indices are chosen
	 * to compare values from the left and right of the sub array to the pivot, in a
	 * loop. When the left index associates to an element with a smaller x-coord
	 * than that of the pivot, the index is incremented. Just so, the right index is
	 * decremented when the element at that index x-coord is greater than the pivot.
	 * When the while loop executes in the next iteration, the comparisons begin at
	 * the next beginning index positions for i and j. After the inner comparison
	 * loops end, if i is less than j, a call to interchange is made, to swap those
	 * elements at those indices. Once elements have been ordered into their proper
	 * positions, the partition is found by calculating the triangular area formed
	 * by the line segment formed by m and p, and another point in the array. The
	 * index of that element is returned as the sub problem partition and all points
	 * should be properly ordered in the array according to their x-coord
	 * positioning to the partition.
	 * 
	 * @param a integer array to be partitioned
	 * @param m left-most sub array index
	 * @param p right-most sub array index
	 * @return last partitioned index
	 */
	private int partition(int[][] a, int m, int p) {

		int[] v = a[m + 1]; // temporarily store value at mth + 1 element as the pivot
		int i = m; // set i to index m + 1, since m must be leftmost point
		int j = p; // set j to index p + 1, since p must be right most point

		int part = 0; //index of partition point
		int partDist = 0; //distance of partition to line segment
		int lineDist = 0; //variable that holds the distance of a point to line segment
		
		int triArea = 0; // initialize variable to store absolute area of triangle formed by m, p and a
							// point in the array
		int triDet = 0; // initialize variable to store determinant of triangle formed by m, p and a
						// point on the array
		int partArea = 0;// initialize variable to store the triangular area between points m, p and
							// current partition
		int partDet = 0; // initialize variable to store the triangular area between points m, p and
							// current partition

		this.subListLeft = new ArrayList<int[]>();
		this.subListRight = new ArrayList<int[]>();

		partDet = determinant(a[m], a[p], a[j]); // get partition triangle determinant
		partArea = Math.abs(partDet / 2); // get partition triangle area
		
		//point distance calc based on distance calc in https://www.geeksforgeeks.org/quickhull-algorithm-convex-hull/
		partDist = Math.abs((v[0]-a[m][0])*(a[p][1]-a[m][1])-(a[p][0]-a[m][0])*(v[1]-a[m][1]));

		// use j as partition index and find the point in the sub array, where the there
		// is a maximum triangular area
		// with m and p and set the partition to return to that point.
		for (int k = m + 1; k < p; k++) {

			triDet = determinant(a[m], a[p], a[k]); // get point k triangle determinant
			triArea = Math.abs(triDet / 2); // get point k triangle area
			
			//calc distance of current point to line
			lineDist = Math.abs((a[k][0]-a[m][0])*(a[p][1]-a[m][1])-(a[p][0]-a[m][0])*(a[k][1]-a[m][1]));

			// check if current partition triangle area is less than that of the triangle
			// formed by p,q,k
			if (partArea < triArea) {
				
				partDist = lineDist;

				partDet = triDet; // set partition triangle determinant to point k triangle determinant
				partArea = triArea; // set partition triangle area to point k triangle area
				j = k; // set partition to point at k
				

			} else { // if not the case, check for a tie

				// if the absolute areas between the two triangles are the same, compare the
				// angles of the compared points the line segment formed by m and p
				if (partArea == triArea) {
					
					
					// if the angle of point at k to the line segment is greater, set partition to
					// that point index
					if (partDet < triDet || partDist < lineDist) {
						
					//if (partDist < eucDist) {

						partDist = lineDist;
						partDet = triDet; // set partition triangle determinant to point k triangle determinant
						partArea = triArea; // set partition triangle area to point k triangle area
						j = k; // set partition to point at k
						
					}

				}
			}

		}

		part = j; //set partition index to j
		v = a[j]; // set v to the partition point
		
		this.subListLeft.add(a[m]);
		this.subListRight.add(v);
		
		j = p; // reset j to element before the line seg end point p

		// sort points of array to left-right ordering to properly breakdown sub array
		// after this partition
		// execute loop while i is less than j
		while (i < j) {

			// move index to first position of pivot left-hand comparisons
			i = i + 1;

			

			// loop and compare left-hand element values to the pivot
			while (determinant(a[m], v, a[i]) > 0 && i < p) {

				this.subListLeft.add(a[i]);

				i = i + 1; // increment index i

			}

			// move index over to first position of pivot right-hand comparisons
			j = j - 1;

			// loop and compare right-hand element values to the pivot, if they are to the
			// left of line segment <v,p>
			while (determinant(v, a[p], a[j]) > 0 && j > m) {

				if (!this.subListLeft.contains(a[j])) {
					this.subListRight.add(a[j]);
				}

				j = j - 1; // decrement index j

			}

			

		}

		this.subListLeft.add(v);
		this.subListRight.add(a[p]);

		

		return part; // return last partition index j
	}

	/**
	 * This method is a swap method implementation taken from the HSR textbook,
	 * modified to swap elements of a 2-d array. A temporary variable is used to
	 * store the element at point i in order to swap value positions without losing
	 * an element's reference.
	 * 
	 * @param a 2-d array whose elements will be swapped
	 * @param i index of first element to be swapped
	 * @param j index of second element who will be swapped with element at i
	 */
	private void interchange(int[][] a, int i, int j) {

		int[] temp = a[i];
		a[i] = a[j];
		a[j] = temp;

	}

	/**
	 * This method is the getter method for the hull list
	 * 
	 * @return the hullList
	 */
	public List<int[]> getHullList() {
		return hullList;
	}

	/**
	 * This method is the getter method for the hull list
	 * 
	 * @param hullList the hullList to set
	 */
	public void setHullList(List<int[]> hullList) {
		this.hullList = hullList;
	}
}
