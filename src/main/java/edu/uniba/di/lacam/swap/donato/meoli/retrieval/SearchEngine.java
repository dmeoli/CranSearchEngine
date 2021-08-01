package edu.uniba.di.lacam.swap.donato.meoli.retrieval;

import edu.uniba.di.lacam.swap.donato.meoli.collection.CranDoc;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * The {@code SearchEngine} class models the search engine.
 *
 * @author Donato Meoli
 */
@SuppressWarnings("deprecated")
public class SearchEngine {

    private IndexWriter indexWriter;
    private final FSDirectory indexDir;

    /**
     * buildTheSearchEngine
     *
     * @param indexPath path to the folder containing the index
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public SearchEngine(String indexPath) throws IOException {
        indexDir = FSDirectory.open(new File(indexPath).toPath());
    }

    /**
     * Returns the list of stopWords.
     *
     * @return list of stopWords
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    private CharArraySet getStopWordSet() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(File.separator + "stopWords")));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        CharArraySet stopWordSet = new CharArraySet(1000, true);
        while (bufferedReader.ready()) stopWordSet.add(bufferedReader.readLine());
        bufferedReader.close();
        return stopWordSet;
    }

    /**
     * Opens the inverted index.
     *
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public void open() throws IOException {
        Map<String, Analyzer> map = new HashMap<>();
        map.put("Abstract", new edu.uniba.di.lacam.swap.donato.meoli.analyzer.Analyzer(getStopWordSet()));
        Analyzer analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), map);
        Similarity[] similarity = {
                new BM25Similarity(2, (float) 0.89),
                new DFRSimilarity(new BasicModelIn(), new AfterEffectB(), new NormalizationH1()),
                new LMDirichletSimilarity(1500)
        };
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setSimilarity(new MultiSimilarity(similarity));
        indexWriter = new IndexWriter(indexDir, config);
    }

    /**
     * Adds a document from the Cranfield collection to the inverted index.
     *
     * @param cranDoc document from the Cranfield collection
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public void addDocument(CranDoc cranDoc) throws IOException {
        Document document = new Document();
        document.add(new StringField("ID", cranDoc.getID(), Field.Store.YES));
        TextField titleField = new TextField("Title", cranDoc.getTitle(), Field.Store.NO);
        titleField.setBoost((float) 0.8);
        document.add(titleField);
        document.add(new TextField("Authors", cranDoc.getAuthors(), Field.Store.NO));
        document.add(new TextField("Department", cranDoc.getDepartment(), Field.Store.NO));
        TextField abstractField = new TextField("Abstract", cranDoc.getAbstr(), Field.Store.NO);
        abstractField.setBoost((float) 1.2);
        document.add(abstractField);
        indexWriter.addDocument(document);
    }

    /**
     * Search the query in the Cranfield collection.
     *
     * @param cranQuery query for the Cranfield collection
     * @return search results
     * @throws IOException    exception raised following a missed or interrupted IO operation
     * @throws ParseException exception raised following an error in query parsing
     */
    public ArrayList<SearchResult> search(String cranQuery) throws IOException, ParseException {
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Similarity[] similarity = {
                new BM25Similarity(2, (float) 0.89),
                new DFRSimilarity(new BasicModelIn(), new AfterEffectB(), new NormalizationH1()),
                new LMDirichletSimilarity(1500)
        };
        indexSearcher.setSimilarity(new MultiSimilarity(similarity));
        String[] fields = {"Title", "Abstract"};
        String queryString = QueryParser.escape(cranQuery);
        Map<String, Analyzer> map = new HashMap<>();
        map.put("Abstract", new edu.uniba.di.lacam.swap.donato.meoli.analyzer.Analyzer(getStopWordSet()));
        Analyzer analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), map);
        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse(queryString);
        TopDocs topDocs = indexSearcher.search(query, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<SearchResult> resultDocs = new ArrayList<>();
        int i = 1;
        for (ScoreDoc scoreDoc : scoreDocs) {
            String currentID = indexSearcher.doc(scoreDoc.doc).get("ID");
            SearchResult currentDoc = new SearchResult(currentID, scoreDoc.score, i++);
            resultDocs.add(currentDoc);
        }
        return resultDocs;
    }

    /**
     * Closes the inverted index.
     *
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public void close() throws IOException {
        indexWriter.close();
    }
}
