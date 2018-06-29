package edu.uniba.di.lacam.swap.donato.meoli;

import edu.uniba.di.lacam.swap.donato.meoli.collection.CranQuery;
import edu.uniba.di.lacam.swap.donato.meoli.retrieval.CranIndexer;
import edu.uniba.di.lacam.swap.donato.meoli.retrieval.SearchEngine;
import edu.uniba.di.lacam.swap.donato.meoli.retrieval.SearchResult;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * La classe {@code CranSearcher} fornisce i risultati della ricerca
 * delle 225 query sui 1400 documenti della collezione Cranfield.
 * @author Donato Meoli
 */
public class CranSearcher {

    private static final String CURRENT_DIR_PATH = System.getProperty("user.dir");
    private static final String COLLECTION_PATH = CURRENT_DIR_PATH + "/cran/collection";
    private static final String DOCS_PATH = CURRENT_DIR_PATH + "/cran/docs";
    private static final String INDEX_PATH = CURRENT_DIR_PATH + "/cran/index";
    private static final String QUERIES_PATH = CURRENT_DIR_PATH + "/cran/queries";
    private static final String RESULT_PATH = CURRENT_DIR_PATH + "/cran";

    /**
     * Funzione main per il calcolo dei risultati delle 225 query sui 1400 documenti della collezione Cranfield.
     * @param args array di stringhe in cui vengono memorizzati i parametri passati al programma
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     * @throws ParseException eccezione sollevata in seguito ad un errore nel parsing della query
     */
    public static void main(String[] args) throws IOException, ParseException {

        CranIndexer cranIndexer = new CranIndexer(COLLECTION_PATH, DOCS_PATH, QUERIES_PATH);
        SearchEngine searchEngine = new SearchEngine(INDEX_PATH, RESULT_PATH);

        searchEngine.open();

        cranIndexer.createDocs();
        File docsDir = new File(DOCS_PATH);
        File[] filesDoc = docsDir.listFiles();
        int i = 0;
        for (File file: Objects.requireNonNull(filesDoc)) {
            if (file.isFile() && !file.getPath().endsWith(".gitkeep")) {
                searchEngine.addDocument(cranIndexer.makeDoc(++i));
            }
        }

        searchEngine.close();

        cranIndexer.createQueries();

        File queryDir = new File(QUERIES_PATH);
        File[] filesQuery = queryDir.listFiles();
        FileWriter resultSet = new FileWriter(RESULT_PATH + "/results");
        int j = 1;
        while (j < Objects.requireNonNull(filesQuery).length) {
            CranQuery query = cranIndexer.makeQuery(j++);
            String result = "";
            String queryString = query.getAbstr();
            ArrayList<SearchResult> resultList = searchEngine.search(queryString);
            for (SearchResult currentResult: resultList) {
                resultSet.append(query.getID()).append(" 0 ").append(currentResult.getID());
                resultSet.append(" ").append(String.valueOf(currentResult.getRank())).append(" ");
                resultSet.append(String.valueOf(currentResult.getScore())).append(" exp_0").append("\n");
            }
            resultSet.write(result);
        }
        resultSet.close();
    }
}
