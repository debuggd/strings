package com.debuggd.text.regex.tokenizer;

public class WhitespaceTokenizer extends StringTokenizer {

	public WhitespaceTokenizer() {
		super( " +" , "\t+" , "\r+" , "\n+" , "\f+" ,
				"\u000B+" , "\u001C+" , "\u001D+" , "\u001E+" , "\u001F+" ,
				"\u00A0+" , "\u2007+" , "\u202F+" ,
				new String( new byte[] { Character.SPACE_SEPARATOR } ) ,
				new String( new byte[] { Character.LINE_SEPARATOR } ) ,
				new String( new byte[] { Character.PARAGRAPH_SEPARATOR } ) );
	}

}
