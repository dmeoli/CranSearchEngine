package edu.uniba.di.lacam.swap.donato.meoli.analyzer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * La classe {@code Analyzer} modella l'analizzatore basato su stopWords.
 *
 * @author Donato Meoli
 */
@SuppressWarnings("deprecation")
public class Analyzer extends StopwordAnalyzerBase {

    /**
     * Costruisce l'analizzatore.
     *
     * @param stopWords elenco delle stopWords
     */
    public Analyzer(CharArraySet stopWords) {
        super(Version.LATEST, stopWords);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(reader);
        TokenStream tokenStream = new StandardFilter(Version.LATEST, tokenizer);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new StopFilter(Version.LATEST, tokenStream, stopwords);
        tokenStream = new KStemFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }
}
