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
import com.debuggd.text.regex.tokenizer.Tokenizer;
import simmetrics.tokenisers.TokenizerWhitespace;

import java.util.List;

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.matchingcoefficient Description:
 * uk.ac.shef.wit.simmetrics.similaritymetrics.matchingcoefficient implements a
 * <p>
 * Date: 02-Apr-2004 Time: 14:31:40
 *
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class MatchingCoefficient implements SimilarityMetric {

	/**
	 * a constant for calculating the estimated timing cost.
	 */
	private final float		ESTIMATEDTIMINGCONST	= 2.0e-4f;

	/**
	 * private tokeniser for tokenisation of the query strings.
	 */
	private final Tokenizer	tokenizer;

	/**
	 * constructor - default (empty).
	 */
	public MatchingCoefficient() {
		tokenizer = new TokenizerWhitespace();
	}

	/**
	 * constructor.
	 *
	 * @param tokenizerToUse
	 *            - the tokeniser to use should a different tokeniser be required
	 */
	public MatchingCoefficient( final Tokenizer tokenizerToUse ) {
		tokenizer = tokenizerToUse;
	}

	/**
	 * returns the string identifier for the metric .
	 *
	 * @return the string identifier for the metric
	 */
	public String getShortDescriptionString() {
		return "MatchingCoefficient";
	}

	/**
	 * returns the long string identifier for the metric.
	 *
	 * @return the long string identifier for the metric
	 */
	public String getLongDescriptionString() {
		return "Implements the Matching Coefficient algorithm providing a similarity measure between two strings";
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
		// 0 0.01 0.03 0.05 0.09 0.12 0.17 0.23 0.3 0.36 0.45 0.53 0.63 0.73 0.85 0.95 1.1 1.21 1.4 1.49 1.69 1.83 2.16 2.18 2.74 2.54 3.46
		// 2.94 3.9 3.38 4.23 3.98 5.49 4.41 5.83 4.95 6.55 5.49 6.77 6.15 7.81 6.55 9.27 7.52 9.23 8.12 11.28 8.83 11.94 9.71 11.94 10.15
		// 12.69 11.28 13.53 11.94 15.62 13.6 16.92 13.53
		final float str1Tokens = tokenizer.tokenizeToList( string1 ).size();
		final float str2Tokens = tokenizer.tokenizeToList( string2 ).size();
		return ( str2Tokens * str1Tokens ) * ESTIMATEDTIMINGCONST;
	}

	/**
	 * gets the similarity of the two strings using MatchingCoefficient.
	 *
	 * @param str1
	 * @param str2
	 * @return a value between 0-1 of the similarity
	 */
	public double getSimilarity( final String str1 , final String str2 ) {
		final List< String > str1Tokens = tokenizer.tokenizeToList( str1 );
		final List< String > str2Tokens = tokenizer.tokenizeToList( str2 );

		final int totalPossible = Math.max( str1Tokens.size() , str2Tokens.size() );
		return getAbsoluteSimilarity( str1 , str2 ) / ( float ) totalPossible;
	}

	/**
	 * gets the un-normalised similarity measure of the metric for the given strings.
	 *
	 * @param str1
	 * @param str2
	 * @return returns the score of the similarity measure (un-normalised)
	 */
	public double getAbsoluteSimilarity( String str1 , String str2 ) {
		final List< String > str1Tokens = tokenizer.tokenizeToList( str1 );
		final List< String > str2Tokens = tokenizer.tokenizeToList( str2 );

		int totalFound = 0;
		for ( Object str1Token : str1Tokens ) {
			final String sToken = ( String ) str1Token;
			boolean found = false;
			for ( Object str2Token : str2Tokens ) {
				final String tToken = ( String ) str2Token;
				if ( sToken.equals( tToken ) ) {
					found = true;
				}
			}
			if ( found ) {
				totalFound++;
			}
		}
		return ( double ) totalFound;
	}
}
