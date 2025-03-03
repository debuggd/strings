package com.debuggd.text.regex;

public interface StopWordHandler {

	default boolean isStopWord( String word ) {
		return false;
	}
}
