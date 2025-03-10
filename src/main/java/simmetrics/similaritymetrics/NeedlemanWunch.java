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
import simmetrics.similaritymetrics.costfunctions.InterfaceSubstitutionCost;
import simmetrics.similaritymetrics.costfunctions.SubCost01;

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.needlemanwunch Description: needlemanwunch implements a edit distance function
 * <p/>
 * Date: 24-Mar-2004 Time: 12:30:47
 *
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class NeedlemanWunch implements SimilarityMetric {

	private InterfaceSubstitutionCost	dCostFunc;

	private float						gapCost;

	public NeedlemanWunch() {
		// set the gapCost to a default value
		gapCost = 2.0f;
		// set the default cost func
		dCostFunc = new SubCost01();
	}

	/**
	 * constructor.
	 *
	 * @param costG
	 *            - the cost of a gap
	 */
	public NeedlemanWunch( final float costG ) {
		// set the gapCost to a given value
		gapCost = costG;
		// set the cost func to a default function
		dCostFunc = new SubCost01();
	}

	/**
	 * constructor.
	 *
	 * @param costG
	 *            - the cost of a gap
	 * @param costFunc
	 *            - the cost function to use
	 */
	public NeedlemanWunch( final float costG , final InterfaceSubstitutionCost costFunc ) {
		// set the gapCost to the given value
		gapCost = costG;
		// set the cost func
		dCostFunc = costFunc;
	}

	/**
	 * constructor.
	 *
	 * @param costFunc
	 *            - the cost function to use
	 */
	public NeedlemanWunch( final InterfaceSubstitutionCost costFunc ) {
		// set the gapCost to a default value
		gapCost = 2.0f;
		// set the cost func
		dCostFunc = costFunc;
	}

	/**
	 * gets the gap cost for the distance function.
	 *
	 * @return the gap cost for the distance function
	 */
	public float getGapCost() {
		return gapCost;
	}

	/**
	 * sets the gap cost for the distance function.
	 *
	 * @param gapCost
	 *            the cost of a gap
	 */
	public void setGapCost( final float gapCost ) {
		this.gapCost = gapCost;
	}

	/**
	 * get the d(i,j) cost function.
	 *
	 * @return InterfaceSubstitutionCost cost function used
	 */
	public InterfaceSubstitutionCost getdCostFunc() {
		return dCostFunc;
	}

	/**
	 * sets the d(i,j) cost function used .
	 *
	 * @param dCostFunc
	 *            - the cost function to use
	 */
	public void setdCostFunc( final InterfaceSubstitutionCost dCostFunc ) {
		this.dCostFunc = dCostFunc;
	}

	/**
	 * gets the similarity of the two strings using Needleman Wunch distance.
	 *
	 * @param str1
	 * @param str2
	 * @return a value between 0-1 of the similarity
	 */
	public double getSimilarity( final String str1 , final String str2 ) {
		float needlemanWunch = getAbsoluteSimilarity( str1 , str2 );

		// normalise into zero to one region from min max possible
		float maxValue = Math.max( str1.length() , str2.length() );
		float minValue = maxValue;
		if ( dCostFunc.getMaxCost() > gapCost ) {
			maxValue *= dCostFunc.getMaxCost();
		}
		else {
			maxValue *= gapCost;
		}
		if ( dCostFunc.getMinCost() < gapCost ) {
			minValue *= dCostFunc.getMinCost();
		}
		else {
			minValue *= gapCost;
		}
		if ( minValue < 0.0f ) {
			maxValue -= minValue;
			needlemanWunch -= minValue;
		}

		// check for 0 maxLen
		if ( maxValue == 0 ) {
			return 1.0f; // as both strings identically zero length
		}
		else {
			// return actual / possible NeedlemanWunch distance to get 0-1 range
			return 1.0f - ( needlemanWunch / maxValue );
		}

	}

	/**
	 * implements the NeedlemanWunch distance function.
	 *
	 * @param str1
	 * @param str2
	 * @return the NeedlemanWunch distance for the given strings
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

		// create matrix (n+1)x(m+1)
		d = new float[n + 1][m + 1];

		// put row and column numbers in place
		for ( i = 0 ; i <= n ; i++ ) {
			d[i][0] = i;
		}
		for ( j = 0 ; j <= m ; j++ ) {
			d[0][j] = j;
		}

		// cycle through rest of table filling values from the lowest cost value of the three part cost function
		for ( i = 1 ; i <= n ; i++ ) {
			for ( j = 1 ; j <= m ; j++ ) {
				// get the substution cost
				cost = dCostFunc.getCost( str1 , i - 1 , str2 , j - 1 );

				// find lowest cost at point from three possible
				d[i][j] = Math.min( d[i - 1][j] + gapCost , Math.min( d[i][j - 1] + gapCost , d[i - 1][j - 1] + cost ) );
			}
		}

		// return bottom right of matrix as holds the maximum edit score
		return d[n][m];
	}
}
