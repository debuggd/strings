/**
 * SimMetrics - SimMetrics is a java library of Similarity or Distance
 * Metrics, e.g. LevenshteinDistance Distance, that provide float based similarity
 * measures between String Data. All metrics return consistant measures
 * rather than unbounded similarity scores.
 *
 * Copyright (C) 2005 Sam Chapman - Open Source Release v1.1
 *
 * Please Feel free to contact me about this library, I would appreciate
 * knowing quickly what you wish to use it for and any criticisms/comments
 * upon the SimMetric library.
 *
 * email:       s.chapman@dcs.shef.ac.uk
 * www:         http://www.dcs.shef.ac.uk/~sam/
 * www:         http://www.dcs.shef.ac.uk/~sam/stringmetrics.html
 *
 * address:     Sam Chapman,
 *              Department of Computer Science,
 *              University of Sheffield,
 *              Sheffield,
 *              S. Yorks,
 *              S1 4DP
 *              United Kingdom,
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package simmetrics.similaritymetrics;

import com.debuggd.text.regex.similarity.abstracts.SimilarityMetric;
import simmetrics.similaritymetrics.costfunctions.AbstractAffineGapCost;
import simmetrics.similaritymetrics.costfunctions.AffineGap5_1;
import simmetrics.similaritymetrics.costfunctions.InterfaceSubstitutionCost;
import simmetrics.similaritymetrics.costfunctions.SubCost5_3_Minus3;

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics Description: SmithWatermanGotohWindowedAffine implements the smith waterman with
 * gotoh extension using a windowed affine gap. Date: 23-Apr-2004 Time: 14:25:30
 *
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public class SmithWatermanGotohWindowedAffine implements SimilarityMetric {

	/**
	 * a constant for calculating the estimated timing cost.
	 */
	private final float					ESTIMATEDTIMINGCONST	= 4.5e-5f;

	/**
	 * private field for the maximum affine gap window size.
	 */
	private final int					windowSize;

	/**
	 * the private cost function used in the SmithWatermanGotoh distance.
	 */
	private InterfaceSubstitutionCost	dCostFunc;

	/**
	 * the private cost function for affine gaps.
	 */
	private AbstractAffineGapCost		gGapFunc;

	/**
	 * constructor - default (empty).
	 */
	public SmithWatermanGotohWindowedAffine() {
		// set the default gap cost func
		gGapFunc = new AffineGap5_1();
		// set the default cost func
		dCostFunc = new SubCost5_3_Minus3();
		// set the default windowSize
		windowSize = 100;
	}

	/**
	 * constructor.
	 *
	 * @param gapCostFunc
	 *            - the gap cost function
	 */
	public SmithWatermanGotohWindowedAffine( final AbstractAffineGapCost gapCostFunc ) {
		// set the gap cost func
		gGapFunc = gapCostFunc;
		// set the cost func to a default function
		dCostFunc = new SubCost5_3_Minus3();
		// set the default window size
		windowSize = 100;
	}

	/**
	 * constructor.
	 *
	 * @param gapCostFunc
	 *            - the cost of a gap
	 * @param costFunc
	 *            - the cost function to use
	 */
	public SmithWatermanGotohWindowedAffine( final AbstractAffineGapCost gapCostFunc , final InterfaceSubstitutionCost costFunc ) {
		// set the gap cost func
		gGapFunc = gapCostFunc;
		// set the cost func
		dCostFunc = costFunc;
		// set the default window size
		windowSize = 100;
	}

	/**
	 * constructor.
	 *
	 * @param costFunc
	 *            - the cost function to use
	 */
	public SmithWatermanGotohWindowedAffine( final InterfaceSubstitutionCost costFunc ) {
		// set the gapCost to a default value
		gGapFunc = new AffineGap5_1();
		// set the cost func
		dCostFunc = costFunc;
		// set the default window size
		windowSize = 100;
	}

	/**
	 * constructor.
	 *
	 * @param affineGapWindowSize
	 *            the size of the affine gap window to use
	 */
	public SmithWatermanGotohWindowedAffine( final int affineGapWindowSize ) {
		// set the default gap cost func
		gGapFunc = new AffineGap5_1();
		// set the default cost func
		dCostFunc = new SubCost5_3_Minus3();
		// set the default windowSize
		windowSize = affineGapWindowSize;
	}

	/**
	 * constructor.
	 *
	 * @param gapCostFunc
	 *            - the gap cost function
	 * @param affineGapWindowSize
	 *            the size of the affine gap window to use
	 */
	public SmithWatermanGotohWindowedAffine( final AbstractAffineGapCost gapCostFunc , final int affineGapWindowSize ) {
		// set the gap cost func
		gGapFunc = gapCostFunc;
		// set the cost func to a default function
		dCostFunc = new SubCost5_3_Minus3();
		// set the default window size
		windowSize = affineGapWindowSize;
	}

	/**
	 * constructor.
	 *
	 * @param gapCostFunc
	 *            - the cost of a gap
	 * @param costFunc
	 *            - the cost function to use
	 * @param affineGapWindowSize
	 *            the size of the affine gap window to use
	 */
	public SmithWatermanGotohWindowedAffine( final AbstractAffineGapCost gapCostFunc , final InterfaceSubstitutionCost costFunc , final int affineGapWindowSize ) {
		// set the gap cost func
		gGapFunc = gapCostFunc;
		// set the cost func
		dCostFunc = costFunc;
		// set the default window size
		windowSize = affineGapWindowSize;
	}

	/**
	 * constructor.
	 *
	 * @param costFunc
	 *            - the cost function to use
	 * @param affineGapWindowSize
	 *            the size of the affine gap window to use
	 */
	public SmithWatermanGotohWindowedAffine( final InterfaceSubstitutionCost costFunc , final int affineGapWindowSize ) {
		// set the gapCost to a default value
		gGapFunc = new AffineGap5_1();
		// set the cost func
		dCostFunc = costFunc;
		// set the default window size
		windowSize = affineGapWindowSize;
	}

	/**
	 * get the g gap cost function.
	 *
	 * @return the gap cost function used
	 */
	public final AbstractAffineGapCost getgGapFunc() {
		return gGapFunc;
	}

	/**
	 * set the g gap cost function with the one provided.
	 *
	 * @param gGapFunc
	 *            - the gap cost function provided
	 */
	public final void setgGapFunc( final AbstractAffineGapCost gGapFunc ) {
		this.gGapFunc = gGapFunc;
	}

	/**
	 * get the d(i,j) cost function.
	 *
	 * @return InterfaceSubstitutionCost cost function used
	 */
	public final InterfaceSubstitutionCost getdCostFunc() {
		return dCostFunc;
	}

	/**
	 * sets the d(i,j) cost function used.
	 *
	 * @param dCostFunc
	 *            - the cost function to use
	 */
	public final void setdCostFunc( final InterfaceSubstitutionCost dCostFunc ) {
		this.dCostFunc = dCostFunc;
	}

	/**
	 * returns the string identifier for the metric.
	 *
	 * @return the string identifier for the metric
	 */
	public String getShortDescriptionString() {
		return "SmithWatermanGotohWindowedAffine";
	}

	/**
	 * returns the long string identifier for the metric.
	 *
	 * @return the long string identifier for the metric
	 */
	public String getLongDescriptionString() {
		return "Implements the Smith-Waterman-Gotoh algorithm with a windowed affine gap providing a similarity measure between two string";
	}

	/**
	 * gets a div class xhtml similarity explaining the operation of the metric.
	 *
	 * @param string1
	 *            string 1
	 * @param string2
	 *            string 2
	 * @return a div class html section detailing the metric operation.
	 */
	public String getSimilarityExplained( String string1 , String string2 ) {
		// todo this should explain the operation of a given comparison
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * gets the estimated time in milliseconds it takes to perform a similarity timing.
	 *
	 * @param string1
	 *            string 1
	 * @param string2
	 *            string 2
	 * @return the estimated time in milliseconds taken to perform the similarity measure
	 */
	public float getSimilarityTimingEstimated( final String string1 , final String string2 ) {
		// timed millisecond times with string lengths from 1 + 50 each increment
		// 0 15.62 62.5 179.5 360 609 891 1297 1640 2125 2688 3297 3984 4766 5485 6437 7313 8312 9391 10500 12704 12938 14359 15704 17266
		// 18844 20360 23547 23845 25688 27656 29532 32048 33891 35844 37938 40251 42610 45001 47407 50142 52266 55314 57970 60782 63814
		// 66470 70376 72767 75861 79283 82564 85814 89408 92658 96283 100080 103283 107518 111033
		final float str1Length = string1.length();
		final float str2Length = string2.length();
		return ( ( str1Length * str2Length * windowSize ) + ( str1Length * str2Length * windowSize ) ) * ESTIMATEDTIMINGCONST;
	}

	/**
	 * gets the similarity of the two strings using Smith-Waterman-Gotoh distance.
	 *
	 * @param str1
	 * @param str2
	 * @return a value between 0-1 of the similarity
	 */
	public final double getSimilarity( final String str1 , final String str2 ) {
		final float smithWatermanGotoh = getAbsoluteSimilarity( str1 , str2 );

		// normalise into zero to one region from min max possible
		float maxValue = Math.min( str1.length() , str2.length() );
		if ( dCostFunc.getMaxCost() > -gGapFunc.getMaxCost() ) {
			maxValue *= dCostFunc.getMaxCost();
		}
		else {
			maxValue *= -gGapFunc.getMaxCost();
		}

		// check for 0 maxLen
		if ( maxValue == 0 ) {
			return 1.0f; // as both strings identically zero length
		}
		else {
			// return actual / possible NeedlemanWunch distance to get 0-1 range
			return ( smithWatermanGotoh / maxValue );
		}
	}

	/**
	 * implements the Smith-Waterman-Gotoh distance function //see http://www.gen.tcd.ie/molevol/nwswat.html for details.
	 *
	 * @param str1
	 * @param str2
	 * @return the Smith-Waterman-Gotoh distance for the two strings given
	 */
	public float getAbsoluteSimilarity( final String str1 , final String str2 ) {
		final float[][] d; // matrix
		final int n; // length of s
		final int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		float cost; // cost

		// check for zero length input
		n = str1.length();
		m = str2.length();
		if ( n == 0 ) {
			return m;
		}
		if ( m == 0 ) {
			return n;
		}

		// create matrix (n)x(m)
		d = new float[n][m];

		// process first row and column first as no need to consider previous rows/columns
		float maxSoFar = 0.0f;
		for ( i = 0 ; i < n ; i++ ) {
			// get the substution cost
			cost = dCostFunc.getCost( str1 , i , str2 , 0 );

			if ( i == 0 ) {
				d[0][0] = Math.max( 0 ,
						cost );
			}
			else {
				float maxGapCost = 0.0f;
				int windowStart = i - windowSize;
				if ( windowStart < 1 ) {
					windowStart = 1;
				}
				for ( int k = windowStart ; k < i ; k++ ) {
					maxGapCost = Math.max( maxGapCost , d[i - k][0] - gGapFunc.getCost( str1 , i - k , i ) );
				}
				d[i][0] = Math.max( 0 , Math.max( maxGapCost , cost ) );
			}
			// update max possible if available
			if ( d[i][0] > maxSoFar ) {
				maxSoFar = d[i][0];
			}
		}
		for ( j = 0 ; j < m ; j++ ) {
			// get the substution cost
			cost = dCostFunc.getCost( str1 , 0 , str2 , j );

			if ( j == 0 ) {
				d[0][0] = Math.max( 0 ,
						cost );
			}
			else {
				float maxGapCost = 0.0f;
				int windowStart = j - windowSize;
				if ( windowStart < 1 ) {
					windowStart = 1;
				}
				for ( int k = windowStart ; k < j ; k++ ) {
					maxGapCost = Math.max( maxGapCost , d[0][j - k] - gGapFunc.getCost( str2 , j - k , j ) );
				}
				d[0][j] = Math.max( 0 , Math.max(
						maxGapCost ,
						cost ) );
			}
			// update max possible if available
			if ( d[0][j] > maxSoFar ) {
				maxSoFar = d[0][j];
			}
		}

		// cycle through rest of table filling values from the lowest cost value of the three part cost function
		for ( i = 1 ; i < n ; i++ ) {
			for ( j = 1 ; j < m ; j++ ) {
				// get the substution cost
				cost = dCostFunc.getCost( str1 , i , str2 , j );

				// find lowest cost at point from three possible
				float maxGapCost1 = 0.0f;
				float maxGapCost2 = 0.0f;
				int windowStart = i - windowSize;
				if ( windowStart < 1 ) {
					windowStart = 1;
				}
				for ( int k = windowStart ; k < i ; k++ ) {
					maxGapCost1 = Math.max( maxGapCost1 , d[i - k][j] - gGapFunc.getCost( str1 , i - k , i ) );
				}
				windowStart = j - windowSize;
				if ( windowStart < 1 ) {
					windowStart = 1;
				}
				for ( int k = windowStart ; k < j ; k++ ) {
					maxGapCost2 = Math.max( maxGapCost2 , d[i][j - k] - gGapFunc.getCost( str2 , j - k , j ) );
				}
				d[i][j] = Math.max( Math.max( 0 ,
						maxGapCost1 ) , Math.max(
								maxGapCost2 ,
								d[i - 1][j - 1] + cost ) );
				// update max possible if available
				if ( d[i][j] > maxSoFar ) {
					maxSoFar = d[i][j];
				}
			}
		}

		// debug output
		/*
		 * System.out.print(" \t"); for (j = 0; j < m; j++) { System.out.print(t.charAt(j) + "     "); } System.out.print("\n"); for (i = 0;
		 * i < n; i++) { //print characteer of string s System.out.print(s.charAt(i) + "\t"); for (j = 0; j < m; j++) {
		 * System.out.print(d[i][j] + "  "); } System.out.print("\n"); }
		 */ // end debug output

		// return max value within matrix as holds the maximum edit score
		return maxSoFar;
	}
}
