package final_project_cs5310_nearchou_kubath_1192018;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * Assignment: CS 5310 Final Project
 * Authors: Ioannis Nearchou and Jonah Kubath
 * Class: CS 5310 Algorithms
 * Date: 11/13/2018
 * Submission Date: 12/6/2018
 * Description: This program is designed answer the final project question
 * regarding if quick sort partition optimizations can be used to optimize
 * quick hull performance. Quick hull is an implementation of convex hull
 * drawing, which finds the points that form a polygon around a graph of
 * points in a given space. Quick sort is implemented similarly to quick
 * sort, where recursive calls of hull() finding with partitioning are done to handle subproblems
 * of finding these polygon points, for given sub arrays of 2-tuples representing
 * point coordinates on an associated graph. So, because of this behavior,
 * could similar optimizations be utilized for a quick hull? Attempts with
 * a few partitioning strategies are performed with randomization of a pivot
 * point, using two pivots and finding a median point in the array of points
 * of a subproblem are investigated. One class for each strategy is created
 * and will be instantiated in the main method for time-complexity comparisons
 * for running a quick hull call. Output comparing initially created randomized
 * graphs and their hulls will be output of for a small n = 10 vertices on the console,
 * to show the resulting computed hulls. Times are taken on average of 5 runs each
 * for each method call and saved to a local csv file for graphing and analysis in
 * an associated report.
 * 
 * 
 * To run this program on the eclipse IDE, simply use the run button
 * and the program will execute a test for the performances of 5 runs
 * of creating randomized graphs for varying n, then reading file
 * input of graphs for a small input problem and the city problem studied in
 * the lecture, then outputting the min cost tree edges and costs in their
 * respective output files. Recorded performance
 * times and sequences are written in the main method, to files within the
 * project folder.
 * 
 *
 */
public class Application {

	public static void main(String[] args) {

		try {

			int runs = 5; // set number of runs for averaging time complexities

			// output csv for analysis of time-complexities
			FileWriter outputTimes = null;
			int r = 15; // initialize variable for subdividing sub arrays in median of medians

			// variables for getting time accumulations of each quick hull for the number of
			// runs
			// chosen
			long rStartTime = 0, rStopTime = 0, rTotalTime = 0;
			long mmStartTime = 0, mmStopTime = 0, mmTotalTime = 0;
			long dpStartTime = 0, dpStopTime = 0, dpTotalTime = 0;

			// variable for holding the calculated average time of each quick hull
			double rAvgTime = 0.0, mmAvgTime = 0.0, dpAvgTime = 0.0;

			// 2-d arrays for a graph of points in two representations
			int[][] graph = null;
			int[][] graphPoints = null;
			char[][] hullPlot = null; // declare variable for 2-d char array for drawing random graph
			char[][] graphPlot = null; // declare variable for 2-d char array for drawing graph's hull

			int[][] tempPoints = null;

			RHullCreator hullC1 = new RHullCreator();
			MMHullCreator hullC2 = new MMHullCreator();
			DPHullCreator hullC3 = new DPHullCreator();

			// declare variables to reference resulting hulls
			int[][] hull1 = null;
			int[][] hull2 = null;
			int[][] hull3 = null;

			hullC2.setR(r);

			List<int[]> pointsList = new LinkedList<int[]>();

			// rng used for deciding randomized edge cost and if two vertices are connected
			// by an edge
			Random rand = new Random();

			// initialize reference for quick hull variant time-complexity output
			outputTimes = new FileWriter("finalprojectcs5310_nearchou_kubath_11232018.csv", false);

			// write metadata title to .csv file
			outputTimes.write(String.format("%s,%s,%s,%s\n", "n", "Avg. Random Hull Time (ms)",
					"MM Random Hull Time (ms)", "Avg. DP Hull Time (ms)"));

			// loop for creating random n*n arrays with edges for measuring kruskal's
			for (int n = 10; n <= 500; n += 10) {

				// instantiate a graph and its cost adjacency matrix
				graph = new int[n][n];

				if (n == 10) {
					graphPlot = new char[n][n];
				}

				// perform double for loop to populate cost and graph with random data
				for (int i = 0; i < n; i++) {

					for (int j = i; j < n; j++) {

						// randomly determine if point exists at i,j (0 for no point, 1 for point)
						graph[i][j] = rand.nextInt(2);

						// if there is a point, add point to points list
						if (graph[i][j] == 1) {
							pointsList.add(new int[2]);
							pointsList.get(pointsList.size() - 1)[0] = i;
							pointsList.get(pointsList.size() - 1)[1] = j;

							if (n == 10) {
								graphPlot[i][j] = 'x';
							}

						}

					}
				}

				tempPoints = new int[pointsList.size()][2];

				// populate temporary graph points array to copy to the actual graph points
				// array before each quick hull
				for (int i = 0; i < pointsList.size(); i++) {
					tempPoints[i] = pointsList.get(i);
				}

				// run each quick hull multiple times and average over the runs
				for (int i = 0; i < runs; i++) {

					graphPoints = Arrays.copyOf(tempPoints, tempPoints.length);

					// measure performance for RHullCreator quick hull
					rStartTime = System.nanoTime();
					hull1 = hullC1.quickHull(graphPoints);
					rStopTime = System.nanoTime();

					graphPoints = Arrays.copyOf(tempPoints, tempPoints.length);

					// measure performance for RHullCreator quick hull
					mmStartTime = System.nanoTime();
					hull2 = hullC2.quickHull(graphPoints);
					mmStopTime = System.nanoTime();

					graphPoints = Arrays.copyOf(tempPoints, tempPoints.length);

					// measure performance for RHullCreator quick hull
					dpStartTime = System.nanoTime();
					hull3 = hullC3.quickHull(graphPoints);
					dpStopTime = System.nanoTime();

					rTotalTime += rStopTime - rStartTime; // accumulate total time over runs
					mmTotalTime += mmStopTime - mmStartTime; // accumulate total time over runs
					dpTotalTime += dpStopTime - dpStartTime; // accumulate total time over runs
				}

				rAvgTime = rTotalTime / (runs * 1000000.0); // calculate average performance
				mmAvgTime = mmTotalTime / (runs * 1000000.0); // calculate average performance
				dpAvgTime = dpTotalTime / (runs * 1000000.0); // calculate average performance

				// write input size n and average performance times to output csv file
				outputTimes.write(String.format("%d,%.8f,.8f,.8f\n", n, rAvgTime, mmAvgTime, dpAvgTime));

				if (n == 10) {

					System.out.println("Here is the random graph plot at n = 10:");
					for (int i = 0; i < graphPlot.length; i++) {

						for (int j = 0; j < graphPlot[0].length; j++) {
							System.out.print(graphPlot[i][j] + " ");
						}
						System.out.println();
					}
					System.out.println();

					hullPlot = new char[n][n];

					for (int i = 0; i < hull1.length; i++) {
						hullPlot[hull1[i][0]][hull1[i][1]] = 'x';
					}

					System.out.println("Here is the rhull plot at n = 10:");
					for (int i = 0; i < hullPlot.length; i++) {

						for (int j = 0; j < hullPlot[0].length; j++) {
							System.out.print(hullPlot[i][j] + " ");
						}
						System.out.println();
					}
					System.out.println();

					hullPlot = new char[n][n];

					for (int i = 0; i < hull2.length; i++) {
						hullPlot[hull2[i][0]][hull2[i][1]] = 'x';
					}
					
					System.out.println("Here is the median of medians hull plot at n = 10:");

					for (int i = 0; i < hullPlot.length; i++) {

						for (int j = 0; j < hullPlot[0].length; j++) {
							System.out.print(hullPlot[i][j] + " ");
						}
						System.out.println();
					}
					System.out.println();

					hullPlot = new char[n][n];

					for (int i = 0; i < hull3.length; i++) {
						hullPlot[hull3[i][0]][hull3[i][1]] = 'x';
					}

					System.out.println("Here is the dual pivot hull plot at n = 10:");
					for (int i = 0; i < hullPlot.length; i++) {

						for (int j = 0; j < hullPlot[0].length; j++) {
							System.out.print(hullPlot[i][j] + " ");
						}
						System.out.println();
					}

				}

				pointsList.clear(); // clear list holding points for this n random graph

			}

			outputTimes.close();

		} catch (FileNotFoundException e) {

			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
