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

import java.util.List;

import com.debuggd.text.regex.similarity.abstracts.SimilarityMetric;
import com.debuggd.text.regex.tokenizer.Tokenizer;

import simmetrics.tokenisers.TokenizerWhitespace;

/**
 * Description: ChapmanOrderedNameCompoundSimilarity tests similarity upon the most similar in terms of token based names where the later
 * names are valued higher than earlier names. Surnames are less flexible than
 *
 * @author Sam Chapman, NLP Group, Sheffield Uni, UK (<a href="mailto:sam@dcs.shef.ac.uk">email</a>,
 *         <a href="http://www.dcs.shef.ac.uk/~sam/">website</a>)
 *         <p>
 *         Date: 08-Dec-2005 Time: 16:50:55
 */
public final class ChapmanOrderedNameCompoundSimilarity implements SimilarityMetric {

	/**
	 * private tokeniser for tokenisation of the query strings.
	 */
	final Tokenizer					tokenizer;

	/**
	 * private string metric allowing internal metric to be composed.
	 */
	private final SimilarityMetric	internalStringMetric1	= new Soundex();

	private final SimilarityMetric	internalStringMetric2	= new SmithWaterman();

	public ChapmanOrderedNameCompoundSimilarity() {
		tokenizer = new TokenizerWhitespace();
	}

	public ChapmanOrderedNameCompoundSimilarity( final Tokenizer tokenizerToUse ) {
		tokenizer = tokenizerToUse;
	}

	/**
	 * gets the similarity of the two strings using a shifted weighting where the latter tokens compared are more important than earlier
	 * ones.
	 *
	 * @param str1
	 * @param str2
	 * @return a value between 0-1 of the similarity
	 */
	@Override
	public final double getSimilarity( final String str1 , final String str2 ) {
		// split the strings into tokens for comparison
		final List< ? > str1Tokens = tokenizer.tokenizeToList( str1 );
		final List< ? > str2Tokens = tokenizer.tokenizeToList( str2 );
		int str1TokenNum = str1Tokens.size();
		int str2TokenNum = str2Tokens.size();
		int minTokens = Math.min( str1TokenNum , str2TokenNum );

		float SKEW_AMMOUNT = 1.0f;

		float sumMatches = 0.0f;
		for ( int i = 1 ; i <= minTokens ; i++ ) {
			float strWeightingAdjustment = 1.0f / minTokens
					+ ( minTokens - i + 0.5f - minTokens / 2.0f ) / minTokens * SKEW_AMMOUNT * ( 1.0f / minTokens );
			final String sToken = ( String ) str1Tokens.get( str1TokenNum - i );
			final String tToken = ( String ) str2Tokens.get( str2TokenNum - i );
			final double found1 = internalStringMetric1.getSimilarity( sToken , tToken );
			final double found2 = internalStringMetric2.getSimilarity( sToken , tToken );
			sumMatches += 0.5f * ( found1 + found2 ) * strWeightingAdjustment;
		}
		return sumMatches;
	}
}