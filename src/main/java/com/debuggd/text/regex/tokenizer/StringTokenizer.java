package com.debuggd.text.regex.tokenizer;

import com.debuggd.text.Text;
import com.debuggd.text.regex.StopWordHandler;

import java.util.ArrayList;
import java.util.List;

public class StringTokenizer implements Tokenizer {

    private StopWordHandler stopWordHandler = new StopWordHandler() {
    };
    private final String regex;

    public StringTokenizer(String... delimiters) {
        Text regexBuilder = new Text();
        for (String delimiter : delimiters) {
            regexBuilder.append("(", delimiter, ")|");
        }
        regexBuilder.deleteLastChar();
        this.regex = regexBuilder.toString();
    }

    @Override
    public StopWordHandler getStopWordHandler() {
        return stopWordHandler;
    }

    @Override
    public void setStopWordHandler(StopWordHandler stopWordHandler) {
        this.stopWordHandler = stopWordHandler;
    }

    @Override
    public List<String> tokenizeToList(String input) {
        String[] tokensAsArray = input.split(regex);
        ArrayList<String> tokens = new ArrayList<>(tokensAsArray.length);
        for (String token : tokensAsArray)
            if (!Text.anyBlank(token) && !stopWordHandler.isStopWord(token))
                tokens.add(token);
        return tokens;
    }
}
