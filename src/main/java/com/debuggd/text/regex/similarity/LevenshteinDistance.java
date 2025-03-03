package com.debuggd.text.regex.similarity;

import com.debuggd.text.Text;
import com.debuggd.text.regex.similarity.abstracts.SimilarityMetric;

public final class LevenshteinDistance implements SimilarityMetric {

	public double getSimilarity( String str1 , String str2 ) {
		if ( Text.anyBlank( str1 ) )
			str1 = "";
		if ( Text.anyBlank( str2 ) )
			str2 = "";
		int maxLen = Math.max( str1.length() , str2.length() );
		if ( maxLen == 0 )
			return 1f;
		float distance = getAbsoluteSimilarity( str1 , str2 );
		return 1.0f - ( distance / maxLen );
	}

	public float getAbsoluteSimilarity( String str1 , String str2 ) {
		if ( Text.anyBlank( str1 ) )
			str1 = "";
		if ( Text.anyBlank( str2 ) )
			str2 = "";
		int[][] matrix = new int[str1.length() + 1][str2.length() + 1];
		for ( int i = 0 ; i <= str1.length() ; i++ )
			matrix[i][0] = i;
		for ( int j = 0 ; j <= str2.length() ; j++ )
			matrix[0][j] = j;
		for ( int i = 1 ; i <= str1.length() ; i++ ) {
			for ( int j = 1 ; j <= str2.length() ; j++ ) {
				int cost = str1.charAt( i - 1 ) == str2.charAt( j - 1 ) ? 0 : 1;
				matrix[i][j] = Math.min( matrix[i - 1][j] + 1 ,
						Math.min( matrix[i][j - 1] + 1 , matrix[i - 1][j - 1] + cost ) );
			}
		}
		return matrix[str1.length()][str2.length()];
	}
}