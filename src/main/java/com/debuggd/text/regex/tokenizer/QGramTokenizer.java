package com.debuggd.text.regex.tokenizer;

import com.debuggd.text.regex.StopWordHandler;

import java.util.ArrayList;

import static com.debuggd.text.Text.anyBlank;

public class QGramTokenizer implements Tokenizer {

    private StopWordHandler stopWordHandler = new StopWordHandler() {
    };
    private final int qGramLength;
    protected String headingPadding = "";
    protected String trailingPadding = "";

    public QGramTokenizer(int qGramLength) {
        if (qGramLength < 1)
            throw new IllegalArgumentException("Min length of q-gram is 1");
        this.qGramLength = qGramLength;
    }

    public QGramTokenizer(int qGramLength, String headingAndTrailingPadding) {
        this(qGramLength);
        if (!anyBlank(headingAndTrailingPadding)) {
            this.headingPadding = headingAndTrailingPadding;
            this.trailingPadding = headingAndTrailingPadding;
        }
    }

    public QGramTokenizer(int qGramLength, String headingPadding, String trailingPadding) {
        this(qGramLength);
        if (!anyBlank(headingPadding))
            this.headingPadding = headingPadding;
        if (!anyBlank(trailingPadding))
            this.trailingPadding = trailingPadding;
    }

    @Override
    public StopWordHandler getStopWordHandler() {
        return stopWordHandler;
    }

    @Override
    public void setStopWordHandler(final StopWordHandler stopWordHandler) {
        this.stopWordHandler = stopWordHandler;
    }

    @Override
    public ArrayList<String> tokenizeToList(String input) {
        ArrayList<String> tokens = new ArrayList<>();
        if (anyBlank(input) || qGramLength > input.length())
            return tokens;
        input = headingPadding + input + trailingPadding;
        int curPos = 0;
        int length = input.length() - qGramLength;
        while (curPos <= length) {
            String term = input.substring(curPos, curPos + qGramLength);
            if (!getStopWordHandler().isStopWord(term))
                tokens.add(term);
            curPos++;
        }
        return tokens;
    }
}
