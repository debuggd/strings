package simmetrics.similaritymetrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.debuggd.text.regex.similarity.abstracts.SimilarityMetric;
import com.debuggd.text.regex.tokenizer.Tokenizer;

import simmetrics.tokenisers.TokenizerWhitespace;

public final class TagLink implements SimilarityMetric {

	/**
	 * private DEFAULT_METRIC is the default method that meassures similarity between tokens.
	 */
	private static final SimilarityMetric DEFAULT_METRIC = new TagLinkToken();

	public static void main( String[] args ) {
		TagLink tl = new TagLink();
		System.out.println( tl.getSimilarityExplained( "test1" , "test2" ) );
	}

	/**
	 * private idfMap contains the IDF weights for each token in the dataset.
	 */
	private HashMap< String , Float >	idfMap;
	/**
	 * private characterBasedStringMetric is the method that meassures similarity between tokens.
	 */
	private SimilarityMetric			characterBasedStringMetric;
	/**
	 * private tokeniser for tokenisation of the query strings.
	 */
	private final Tokenizer				tokenizer;

	/**
	 * TagLink default constructor. IDF weights are all equally weighted. Transposition constant value is 0.3
	 */
	public TagLink() {
		this( DEFAULT_METRIC );
	}

	/**
	 * TagLink constructor requires a character based string metric.
	 *
	 * @param characterBasedStringMetric
	 *            CharacterBasedStringMetric
	 */
	public TagLink( SimilarityMetric characterBasedStringMetric ) {
		this.characterBasedStringMetric = characterBasedStringMetric;
		tokenizer = new TokenizerWhitespace();
	}

	/**
	 * TagLink constructor requires dataset data in order to compute the IDF weights. Default character based string metric is TagLinkToken.
	 *
	 * @param dataSetArray
	 *            String[]
	 */
	public TagLink( String[] dataSetArray ) {
		this( dataSetArray , DEFAULT_METRIC );
	}

	/**
	 * TagLink constructor requires dataset data in order to compute the IDF weights. Also requires a character based string metric.
	 *
	 * @param dataSetArray
	 *            String[]
	 * @param characterBasedStringMetric
	 *            CharacterBasedStringMetric
	 */
	public TagLink( String[] dataSetArray ,
			SimilarityMetric characterBasedStringMetric ) {
		this.characterBasedStringMetric = characterBasedStringMetric;
		tokenizer = new TokenizerWhitespace();
		this.idfMap = getIDFMap( dataSetArray );
	}

	/**
	 * returns the long string identifier for the metric.
	 *
	 * @return the long string identifier for the metric
	 */
	public String getLongDescriptionString() {
		return getShortDescriptionString();
	}

	/**
	 * getShortDescriptionString returns the name and parameters of this string metric
	 *
	 * @return String
	 */
	public String getShortDescriptionString() {
		if ( idfMap == null ) {
			return "[TagLink_[" + characterBasedStringMetric.toString() +
					"]";
		}
		else {
			return "[TagLink_IDF_[" + characterBasedStringMetric.toString() +
					"]";
		}

	}

	/**
	 * getSimilarity computes the similarity between a pair of strings T and U.
	 *
	 * @param str1
	 *            String
	 * @param str2
	 *            String
	 * @return float
	 */
	@Override
	public double getSimilarity( String str1 , String str2 ) {
		if ( str1.equals( str2 ) ) {
			return 1.0f;
		}
		else {
			List< String > tArrayList = tokenizer.tokenizeToList( str1 );
			List< String > uArrayList = tokenizer.tokenizeToList( str2 );
			String[] tTokens = tArrayList.toArray( new String[tArrayList.size()] ) ,
					uTokens = uArrayList.toArray( new String[uArrayList.size()] );
			float[] tIdfArray = getIDFArray( tTokens ) ,
					uIdfArray = getIDFArray( uTokens );
			return algorithm1( tTokens , uTokens , tIdfArray , uIdfArray );
		}
	}

	/**
	 * explainStringMetric gives a brief explanation of how the stringMetric was computed.
	 *
	 * @param T
	 *            String
	 * @param U
	 *            String
	 * @return String
	 */
	public String getSimilarityExplained( String T , String U ) {
		StringBuffer buff = new StringBuffer();
		buff.append( "\n\t*****TagLink String Distance*****" );
		if ( T.equals( U ) ) {
			buff.append( "\nS(T,U)=1.0\n" );
		}
		else {
			List< String > tArrayList = tokenizer.tokenizeToList( T );
			List< String > uArrayList = tokenizer.tokenizeToList( U );
			String[] tTokens = tArrayList.toArray( new String[tArrayList.size()] ) ,
					uTokens = uArrayList.toArray( new String[uArrayList.size()] );
			buff.append( "\nT={" );
			for ( String tToken : tTokens ) {
				buff.append( tToken ).append( ", " );
			}
			buff.append( "}\n" );

			buff.append( "U={" );
			for ( String uToken : uTokens ) {
				buff.append( uToken ).append( ", " );
			}
			buff.append( "}\n" );

			float minStringSize = getMinStringSize( tTokens , uTokens );
			buff.append( "min(|T|,|U|)=" ).append( minStringSize ).append( "\n" );

			buff.append( "\nIDF weights:\n" );
			buff.append( "Ti\tai(Ti)\n" );
			float[] tIdfArray = getIDFArray( tTokens ) ,
					uIdfArray = getIDFArray( uTokens );
			for ( int i = 0 ; i < tIdfArray.length ; i++ ) {
				buff.append( tTokens[i] ).append( "\t" ).append( round( tIdfArray[i] ) ).append( "\n" );
			}
			buff.append( "\nUj\taj(Uj)\n" );
			for ( int i = 0 ; i < uIdfArray.length ; i++ ) {
				buff.append( uTokens[i] ).append( "\t" ).append( round( uIdfArray[i] ) ).append( "\n" );
			}
			buff.append( "\nScores:\n" );
			buff.append( "Ti\tUj\tSij(Ti,Uj)\tIDFij(Ti,Uj)\tMRij(Ti,Uj)\tSij\n" );
			ArrayList< Candidates > candidateList = new ArrayList<>();
			for ( int t = 0 ; t < tTokens.length ; t++ ) {
				int lastTr = -1;
				for ( int u = 0 , flag = 0 ; u < uTokens.length && flag == 0 ; u++ ) {
					int tr = Math.abs( t - u );
					if ( lastTr >= 0 && lastTr < tr ) {
						flag = 1;
					}
					else {
						String tTok = tTokens[t] , uTok = uTokens[u];
						double innerScore = characterBasedStringMetric.getSimilarity( tTok ,
								uTok );
						if ( innerScore >= 0.0 ) {
							float MR;
							if ( innerScore == 1.0 ) {
								MR = tTokens[t].length();
							}
							else {
								MR = ( ( TagLinkToken ) characterBasedStringMetric ).getMatched();
							}
							MR = MR / minStringSize;
							float IDF = tIdfArray[t] * uIdfArray[u] ,
									weight = ( IDF + MR ) / 2.0f;
							if ( innerScore == 1 ) {
								lastTr = tr;
							}
							buff.append( tTok ).append( "\t" ).append( uTok ).append( "\t" ).append( round( innerScore ) ).append( "\t" ).append( round( IDF ) ).append( "\t" )
									.append( round( MR ) ).append( "\t" ).append( round( innerScore * weight ) ).append( "\n" );
							candidateList.add( new Candidates( t , u , innerScore * weight ) );
						}
					}
				}
			}
			sortCandidateList( candidateList );

			// iterate the candidate list
			buff.append( "\nCommon tokens (Algorithm 1):\n" );
			buff.append( "Ti\tUj\tSij*Xij\n" );
			float score = 0.0f;
			HashMap< Integer , Object > tMap = new HashMap<>();
			HashMap< Integer , Object > uMap = new HashMap<>();
			for ( Object aCandidateList : candidateList ) {
				Candidates actualCandidates = ( Candidates ) aCandidateList;
				Integer tPos = actualCandidates.getTPos();
				Integer uPos = actualCandidates.getUPos();
				if ( !tMap.containsKey( tPos ) &&
						!uMap.containsKey( uPos ) ) {
					double tokenScore = actualCandidates.getScore();
					score += tokenScore;
					tMap.put( tPos , null );
					uMap.put( uPos , null );
					buff.append( tTokens[tPos] ).append( "\t" ).append( uTokens[uPos] ).append( "\t" ).append( round( tokenScore ) ).append( "\n" );
				}
			}
			buff.append( "\nS(T,U)=" ).append( round( score ) ).append( "\n" );
		}
		return buff.toString();
	}

	/**
	 * algorithm1 select the considered most appropiate token pairs and compute the sum of the selected pairs.
	 *
	 * @param tTokens
	 *            String[]
	 * @param uTokens
	 *            String[]
	 * @param tIdfArray
	 *            float[]
	 * @param uIdfArray
	 *            float[]
	 * @return float
	 */
	private float algorithm1( String[] tTokens , String[] uTokens ,
			float[] tIdfArray , float[] uIdfArray ) {
		ArrayList< Candidates > candidateList = obtainCandidateList( tTokens , uTokens , tIdfArray ,
				uIdfArray );
		sortCandidateList( candidateList );
		float scoreValue = 0.0f;
		HashMap< Integer , Object > tMap = new HashMap<>();
		HashMap< Integer , Object > uMap = new HashMap<>();
		for ( Object aCandidateList : candidateList ) {
			Candidates actualCandidates = ( Candidates ) aCandidateList;
			Integer tPos = actualCandidates.getTPos();
			Integer uPos = actualCandidates.getUPos();
			if ( !tMap.containsKey( tPos ) &&
					!uMap.containsKey( uPos ) ) {
				scoreValue += actualCandidates.getScore();
				tMap.put( tPos , null );
				uMap.put( uPos , null );
			}
		}
		return scoreValue;
	}

	/**
	 * getFrequency retrieve the value of the map.
	 *
	 * @param word
	 *            String
	 * @param map
	 *            Map
	 * @return float
	 */
	private float getFrequency( String word , Map< String , Float > map ) {
		Float frequency = map.get( word );
		if ( frequency == null ) {
			return 0;
		}
		return frequency;
	}

	/**
	 * getIDFArray normalize a vector of IDF weights.
	 *
	 * @param tokenArray
	 *            String[]
	 * @return float[]
	 */
	private float[] getIDFArray( String[] tokenArray ) {
		int tokenArrayLength = tokenArray.length;
		float[] IDFArray = new float[tokenArrayLength];
		if ( idfMap == null ) {
			float cosineWeight = 1.0f / ( float ) Math.sqrt( tokenArrayLength );
			for ( int i = 0 ; i < tokenArrayLength ; i++ ) {
				IDFArray[i] = cosineWeight;
			}
		}
		else {
			float sq = 0f;
			for ( int i = 0 ; i < tokenArrayLength ; i++ ) {
				String actualToken = tokenArray[i];
				float idfWeight = 0.0f;
				try {
					idfWeight = idfMap.get( actualToken );
				}
				catch ( Exception e ) {
					// SAM added this as the catch was unguarded.
					e.printStackTrace();
				}
				IDFArray[i] = idfWeight;
				sq += idfWeight * idfWeight;
			}
			sq = ( float ) Math.sqrt( sq );
			for ( int i = 0 ; i < tokenArrayLength ; i++ ) {
				IDFArray[i] = IDFArray[i] / sq;
			}
		}
		return IDFArray;
	}

	/**
	 * getIDFMap compute the IDF weights for the dataset provided.
	 *
	 * @param dataSetArray
	 *            String[]
	 */
	private HashMap< String , Float > getIDFMap( String[] dataSetArray ) {
		float N = dataSetArray.length;
		HashMap< String , Float > idfMap = new HashMap<>();
		for ( int row = 0 ; row < N ; row++ ) {
			HashMap< String , Object > rowMap = new HashMap<>();
			HashMap< String , Float > freqMap = new HashMap<>();
			String actualRow = dataSetArray[row];
			List< String > tokenArrayList = tokenizer.tokenizeToList( actualRow );
			String[] rowArray = tokenArrayList.toArray( new String[tokenArrayList.size()] );
			for ( String actualToken : rowArray ) {
				rowMap.put( actualToken , null );

				float actualFrequency = getFrequency( actualToken , freqMap ) +
						1.0f;
				freqMap.put( actualToken , actualFrequency );
			}
			Collection< String > entries = rowMap.keySet();
			for ( String actualToken : entries ) {
				float actualFrequency = getFrequency( actualToken , idfMap ) +
						1.0f;
				idfMap.put( actualToken , actualFrequency );
			}

		}

		Collection< Map.Entry< String , Float > > entries = idfMap.entrySet();
		Map.Entry< String , Float > ent;
		for ( Map.Entry< String , Float > entry : entries ) {
			ent = entry;
			String key = ent.getKey();
			float frequency = ent.getValue();
			float idf = ( float ) Math.log( N / frequency + 1.0f );
			idfMap.put( key , idf );
		}
		return idfMap;
	}

	/**
	 * getMinStringSize count the number of characters in String array tTokens and String array uTokens and return the minimun size.
	 *
	 * @param tTokens
	 *            String[]
	 * @param uTokens
	 *            String[]
	 * @return float
	 */
	private float getMinStringSize( String[] tTokens , String[] uTokens ) {
		float tSize = 0 , uSize = 0;
		for ( String tToken : tTokens ) {
			tSize += tToken.length();
		}
		for ( String uToken : uTokens ) {
			uSize += uToken.length();
		}
		return Math.min( tSize , uSize );
	}

	/**
	 * obtainCandidateList set a candidate list of pair of tokens. Sometimes it will not compute all candidate pairs in oder to reduce the
	 * computational cost.
	 *
	 * @param tTokens
	 *            String[]
	 * @param uTokens
	 *            String[]
	 * @param tIdfArray
	 *            float[]
	 * @param uIdfArray
	 *            float[]
	 * @return ArrayList
	 */
	private ArrayList< Candidates > obtainCandidateList( String[] tTokens , String[] uTokens ,
			float[] tIdfArray , float[] uIdfArray ) {
		ArrayList< Candidates > candidateList = new ArrayList<>();
		float minStringSize = getMinStringSize( tTokens , uTokens );
		for ( int t = 0 ; t < tTokens.length ; t++ ) {
			int lastTr = -1;
			for ( int u = 0 , flag = 0 ; u < uTokens.length && flag == 0 ; u++ ) {
				int tr = Math.abs( t - u );
				if ( lastTr >= 0 && lastTr < tr ) {
					flag = 1;
				}
				else {
					String tTok = tTokens[t] , uTok = uTokens[u];
					double innerScore = characterBasedStringMetric.getSimilarity( tTok ,
							uTok );
					if ( innerScore >= 0.0f ) {
						float matched;
						if ( innerScore == 1.0f ) {
							matched = tTokens[t].length();
						}
						else {
							matched = ( ( TagLinkToken ) characterBasedStringMetric ).getMatched();
						}
						float weightMatched = matched / minStringSize ,
								weightTFIDF = tIdfArray[t] * uIdfArray[u] ,
								weight = ( weightTFIDF + weightMatched ) / 2.0f;
						if ( innerScore == 1.0f ) {
							lastTr = tr;
						}
						candidateList.add( new Candidates( t , u , innerScore * weight ) );
					}
				}
			}
		}
		return candidateList;
	}

	/**
	 * round a float number.
	 *
	 * @param number
	 *            float
	 * @return float
	 */
	private double round( double number ) {
		int round = ( int ) ( number * 1000.00f );
		double rest = number * 1000.00f - round;
		if ( rest >= 0.5f ) {
			round++;
		}
		return round / 1000.00f;
	}

	/**
	 * sortCandidateList sort a list of candidate pair of tokens.
	 *
	 * @param list
	 *            ArrayList of candidates
	 */
	private void sortCandidateList( ArrayList< Candidates > list ) {
		Collections.sort( list , ( o1 , o2 ) -> {
			double scoreT = o1.getScore();
			double scoreU = o2.getScore();
			if ( scoreU > scoreT ) {
				return 1;
			}
			if ( scoreU < scoreT ) {
				return -1;
			}
			return 0;
		} );
	}
}

class Candidates implements Serializable {

	private static final long	serialVersionUID	= 7821930353109539930L;
	private int					tPos , uPos;
	private double				score;

	/**
	 * Candidates constructor. Creates an instance of a candidate string pair T and U. It requires the position of the pair in the string
	 * and the score or distance between them.
	 *
	 * @param tPos
	 *            int
	 * @param uPos
	 *            int
	 * @param score
	 *            float
	 */
	public Candidates( int tPos , int uPos , double score ) {
		this.tPos = tPos;
		this.uPos = uPos;
		this.score = score;
	}

	/**
	 * getScore, return the score or distance between strings T and U.
	 *
	 * @return float
	 */
	public double getScore() {
		return score;
	}

	/**
	 * getTPos, return the position of string T.
	 *
	 * @return int
	 */
	public int getTPos() {
		return tPos;
	}

	/**
	 * getUPos, return the position of string U.
	 *
	 * @return int
	 */
	public int getUPos() {
		return uPos;
	}

}