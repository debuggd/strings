package com.debuggd.text.regex.tokenizer;

import com.debuggd.text.regex.StopWordHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Tokenizer {

	public StopWordHandler getStopWordHandler();

	/**
	 * sets the stop word handler used with the handler given.
	 *
	 * @param stopWordHandler
	 *            the given stop word hanlder
	 */
	public void setStopWordHandler( StopWordHandler stopWordHandler );

	/**
	 * Return tokenized version of a string as an ArrayList.
	 *
	 * @param input
	 * @return List tokenized version of a string
	 */
	public List< String > tokenizeToList( String input );

	/**
	 * Return tokenized version of a string as a set.
	 *
	 * @param input
	 * @return tokenized version of a string as a set
	 */
	default Set< String > tokenizeToSet( String input ) {
		final Set< String > returnSet = new HashSet<>();
		returnSet.addAll( tokenizeToList( input ) );
		return returnSet;
	}
}