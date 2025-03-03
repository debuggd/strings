package com.debuggd.text.regex.similarity;

import com.debuggd.text.regex.similarity.abstracts.VectorBasedDistance;

public class BlockDistance extends VectorBasedDistance {

	protected float computeDistance( int nmbTokens1 , int nmbTokens2 ) {
		return ( float ) Math.abs( nmbTokens1 - nmbTokens2 );
	}

	protected float computeDistance( float totalDistance ) {
		return totalDistance;
	}

	protected float computePossibleDistance( int nmbTokens1 , int nmbTokens2 ) {
		return ( float ) ( nmbTokens1 + nmbTokens2 );
	}
}
