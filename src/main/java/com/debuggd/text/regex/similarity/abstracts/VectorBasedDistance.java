package com.debuggd.text.regex.similarity.abstracts;

import com.debuggd.text.regex.similarity.abstracts.SimilarityMetric;
import com.debuggd.text.regex.tokenizer.Tokenizer;
import simmetrics.tokenisers.TokenizerWhitespace;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class VectorBasedDistance implements SimilarityMetric {

	protected Tokenizer tokenizer;

	public VectorBasedDistance() {
		tokenizer = new TokenizerWhitespace();
	}

	public VectorBasedDistance( final Tokenizer tokenizerToUse ) {
		tokenizer = tokenizerToUse;
	}

	public double getSimilarity( final String str1 , final String str2 ) {
		List< String > str1Tokens = tokenizer.tokenizeToList( str1 );
		List< String > str2Tokens = tokenizer.tokenizeToList( str2 );
		float possibleDistance = computePossibleDistance( str1Tokens.size() , str2Tokens.size() );
		if ( possibleDistance == 0f )
			return 0f;
		float distance = getSimilarityImpl( str1Tokens , str2Tokens );
		return ( possibleDistance - distance ) / possibleDistance;
	}

	public float getAbsoluteSimilarity( String str1 , String str2 ) {
		return getSimilarityImpl( tokenizer.tokenizeToList( str1 ) , tokenizer.tokenizeToList( str2 ) );
	}

	protected float getSimilarityImpl( List< String > str1Tokens , List< String > str2Tokens ) {
		Set< String > allTokens = new HashSet<>();
		allTokens.addAll( str1Tokens );
		allTokens.addAll( str2Tokens );
		int totalDistance = 0;
		for ( String token : allTokens ) {
			int nmbTokensInStr1 = countTokens( str1Tokens , token );
			int nmbTokensInStr2 = countTokens( str2Tokens , token );
			totalDistance += computeDistance( nmbTokensInStr1 , nmbTokensInStr2 );
		}
		return computeDistance( totalDistance );
	}

	protected abstract float computeDistance( int nmbTokens1 , int nmbTokens2 );

	protected abstract float computeDistance( float totalDistance );

	protected abstract float computePossibleDistance( int tokensSize1 , int tokensSize2 );

	private static int countTokens( List< String > tokens , String token ) {
		int tokenCounter = 0;
		for ( String str1Token : tokens )
			if ( str1Token.equals( token ) )
				tokenCounter++;
		return tokenCounter;
	}
}
