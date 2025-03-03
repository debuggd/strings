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

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.jarowinkler Description: uk.ac.shef.wit.simmetrics.similaritymetrics.jarowinkler
 * implements a String Metric.
 * <p/>
 * Date: 02-Apr-2004 Time: 11:49:16
 *
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class JaroWinkler implements SimilarityMetric {

	/**
	 * private string metric allowing internal metric to be composed.
	 */
	private final SimilarityMetric	internalStringMetric;

	/**
	 * maximum prefix length to use.
	 */
	private static final int		MINPREFIXTESTLENGTH		= 6;

	/**
	 * prefix adjustment scale.
	 */
	private static final float		PREFIXADUSTMENTSCALE	= 0.1f;

	/**
	 * constructor - default (empty).
	 */
	public JaroWinkler() {
		internalStringMetric = new Jaro();
	}

	/**
	 * gets the similarity measure of the JaroWinkler metric for the given strings.
	 *
	 * @param str1
	 * @param str2
	 * @return 0-1 similarity measure of the JaroWinkler metric
	 */
	public double getSimilarity( final String str1 , final String str2 ) {
		// gets normal Jaro Score
		final double dist = internalStringMetric.getSimilarity( str1 , str2 );

		// This extension modifies the weights of poorly matching pairs string1, string2 which share a common prefix
		final int prefixLength = getPrefixLength( str1 , str2 );
		return dist + ( ( float ) prefixLength * PREFIXADUSTMENTSCALE * ( 1.0f - dist ) );
	}

	/**
	 * gets the un-normalised similarity measure of the metric for the given strings.
	 *
	 * @param str1
	 * @param str2
	 * @return returns the score of the similarity measure (un-normalised)
	 */
	public double getAbsoluteSimilarity( String str1 , String str2 ) {
		return getSimilarity( str1 , str2 );
	}

	private static int getPrefixLength( final String string1 , final String string2 ) {
		final int n = Math.min( MINPREFIXTESTLENGTH , Math.min( string1.length() , string2.length() ) );
		// check for prefix similarity of length n
		for ( int i = 0 ; i < n ; i++ ) {
			// check the prefix is the same so far
			if ( string1.charAt( i ) != string2.charAt( i ) ) {
				// not the same so return as far as got
				return i;
			}
		}
		return n; // first n characters are the same
	}
}
