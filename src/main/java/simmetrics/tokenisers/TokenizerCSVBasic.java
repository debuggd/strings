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

package simmetrics.tokenisers;

import java.io.Serializable;
import java.util.ArrayList;

import com.debuggd.text.regex.StopWordHandler;
import com.debuggd.text.regex.tokenizer.Tokenizer;

public final class TokenizerCSVBasic implements Tokenizer , Serializable {

	private static final long	serialVersionUID	= -7049230409725520896L;
	private StopWordHandler		stopWordHandler		= new StopWordHandler() {};
	private final String		delimiters			= ",";

	public final String getDelimitersoLD() {
		return delimiters;
	}

	@Override
	public StopWordHandler getStopWordHandler() {
		return stopWordHandler;
	}

	@Override
	public void setStopWordHandler( final StopWordHandler stopWordHandler ) {
		this.stopWordHandler = stopWordHandler;
	}

	/**
	 * Return tokenized version of a string .
	 *
	 * @param input
	 * @return tokenized version of a string
	 */
	@Override
	public final ArrayList< String > tokenizeToList( final String input ) {
		final ArrayList< String > returnArrayList = new ArrayList< >();
		int curPos = 0;
		while ( curPos < input.length() ) {
			final char ch = input.charAt( curPos );
			if ( Character.isWhitespace( ch ) ) {
				curPos++;
			}
			int nextGapPos = input.length();
			// check delimitors
			for ( int i = 0 ; i < delimiters.length() ; i++ ) {
				final int testPos = input.indexOf( delimiters.charAt( i ) , curPos );
				if ( testPos < nextGapPos && testPos != -1 ) {
					nextGapPos = testPos;
				}
			}
			// add new token
			final String term = input.substring( curPos , nextGapPos );
			if ( !stopWordHandler.isStopWord( term ) && !term.equals( " " ) ) {
				returnArrayList.add( term );
			}
			curPos = nextGapPos;
		}

		return returnArrayList;
	}
}
