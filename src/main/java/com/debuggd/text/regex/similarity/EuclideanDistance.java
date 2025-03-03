package com.debuggd.text.regex.similarity;

import com.debuggd.text.regex.similarity.abstracts.VectorBasedDistance;

public final class EuclideanDistance extends VectorBasedDistance {

	@Override
	protected float computeDistance( int nmbTokens1 , int nmbTokens2 ) {
		return ( float ) Math.sqrt( ( nmbTokens1 * nmbTokens1 ) + ( nmbTokens2 * nmbTokens2 ) );
	}

	@Override
	protected float computeDistance( float totalDistance ) {
		return ( float ) Math.sqrt( totalDistance );
	}

	@Override
	protected float computePossibleDistance( int nmbTokens1 , int nmbTokens2 ) {
		return ( float ) ( ( nmbTokens1 - nmbTokens2 ) * ( nmbTokens1 - nmbTokens2 ) );
	}
}
