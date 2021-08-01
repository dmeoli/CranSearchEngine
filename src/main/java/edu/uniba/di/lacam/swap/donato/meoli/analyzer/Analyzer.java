package edu.uniba.di.lacam.swap.donato.meoli.analyzer;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * The {@code Analyzer} class models the stopWords-based analyzer.
 *
 * @author Donato Meoli
 */
@SuppressWarnings("deprecated")
public class Analyzer extends StopwordAnalyzerBase {

    /**
     * Build the analyzer.
     *
     * @param stopWords list of stopWords
     */
    public Analyzer(CharArraySet stopWords) {
        super(stopWords);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, stopwords);
        tokenStream = new KStemFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }
}
