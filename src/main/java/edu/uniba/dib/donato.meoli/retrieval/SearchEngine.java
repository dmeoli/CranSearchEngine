package edu.uniba.dib.donato.meoli.retrieval;

import edu.uniba.dib.donato.meoli.collection.CranDoc;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
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
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.apache.lucene.util.Version.LATEST;

/**
 * La classe {@code SearchEngine} modella il motore di ricerca.
 * @author Donato Meoli
 */
@SuppressWarnings({"deprecated", "deprecation"})
public class SearchEngine {

    private IndexWriter indexWriter;
    private FSDirectory indexDir;
    private String resultPath;

    /**
     * Costruisce il motore di ricerca.
     * @param indexPath percorso della cartella contenente l'indice
     * @param resultPath percorso della cartella contenente la collezione di documenti e di query Cranfield
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public SearchEngine(String indexPath, String resultPath) throws IOException {
        indexDir = FSDirectory.open(new File(indexPath));
        this.resultPath = resultPath;
    }

    /**
     * Restituisce l'elenco delle stopwords.
     * @return elenco delle stopwords
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    private CharArraySet getStopWordSet() throws IOException {
        File file = new File(resultPath + "/stopwords");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        CharArraySet stopWordSet = new CharArraySet(LATEST, 1000, true);
        while (bufferedReader.ready()) {
            stopWordSet.add(bufferedReader.readLine());
        }
        bufferedReader.close();
        return stopWordSet;
    }

    /**
     * Apre l'indice invertito.
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public void open() throws IOException {
        Map<String, Analyzer> map = new HashMap<>();
        map.put("Abstract", new edu.uniba.dib.donato.meoli.analyzer.Analyzer(getStopWordSet()));
        Analyzer analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LATEST), map);
        Similarity similarity[] = {
                new BM25Similarity(2, (float) 0.89),
                new DFRSimilarity(new BasicModelIn(), new AfterEffectB(), new NormalizationH1()),
                new LMDirichletSimilarity(1500)
        };
        IndexWriterConfig config = new IndexWriterConfig(LATEST, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setSimilarity(new MultiSimilarity(similarity));
        indexWriter = new IndexWriter(indexDir, config);
    }

    /**
     * Aggiunge un documento della collezione Cranfield all'indice invertito.
     * @param cranDoc documento della collezione Cranfield
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public void addDocument(CranDoc cranDoc) throws IOException {
        Document document = new Document();
        document.add(new StringField("ID", cranDoc.getId(), Field.Store.YES));
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
     * Effettua la ricerca della query nella collezione Cranfield.
     * @param cranQuery query per la collezione Cranfield
     * @return risultati della ricerca
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     * @throws ParseException eccezione sollevata in seguito ad un errore nel parsing della query
     */
    public ArrayList<SearchResult> search(String cranQuery) throws IOException, ParseException {
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Similarity similarity[] = {
                new BM25Similarity(2, (float) 0.89),
                new DFRSimilarity(new BasicModelIn(), new AfterEffectB(), new NormalizationH1()),
                new LMDirichletSimilarity(1500)
        };
        indexSearcher.setSimilarity(new MultiSimilarity(similarity));
        String fields[] = {"Title", "Abstract"};
        String queryString = QueryParser.escape(cranQuery);
        Map<String, Analyzer> map = new HashMap<>();
        map.put("Abstract", new edu.uniba.dib.donato.meoli.analyzer.Analyzer(getStopWordSet()));
        Analyzer analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LATEST), map);
        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse(queryString);
        TopDocs topDocs = indexSearcher.search(query, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<SearchResult> resultDocs = new ArrayList<>();
        int i = 1;
        for (ScoreDoc scoreDoc: scoreDocs) {
            String currentID = indexSearcher.doc(scoreDoc.doc).get("ID");
            SearchResult currentDoc = new SearchResult(currentID, scoreDoc.score, i++);
            resultDocs.add(currentDoc);
        }
        return resultDocs;
    }

    /**
     * Chiude l'indice invertito.
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public void close() throws IOException {
        indexWriter.close();
    }
}
