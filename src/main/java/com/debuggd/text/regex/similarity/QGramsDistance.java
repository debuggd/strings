package com.debuggd.text.regex.similarity;

import com.debuggd.text.regex.tokenizer.QGramTokenizer;

public class QGramsDistance extends BlockDistance {

	public QGramsDistance() {
		tokenizer = new QGramTokenizer( 3 );
	}

	public QGramsDistance( QGramTokenizer tokenizer ) {
		this.tokenizer = tokenizer;
	}
}
