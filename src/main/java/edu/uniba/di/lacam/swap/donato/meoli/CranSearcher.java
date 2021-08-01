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
 * The {@code CranSearcher} class provides the search results of
 * 225 queries on 1400 documents in the Cranfield collection.
 *
 * @author Donato Meoli
 */
public class CranSearcher {

    private static final String CRAN_PATH = System.getProperty("user.dir") + File.separator + "cran";

    private static final String DOCS_PATH = CRAN_PATH + File.separator + "docs";
    private static final String INDEX_PATH = CRAN_PATH + File.separator + "index";
    private static final String QUERIES_PATH = CRAN_PATH + File.separator + "queries";

    /**
     * Main function for calculating the results of 225 queries on 1400 documents in the Cranfield collection.
     *
     * @param args array of strings in which the parameters passed to the program are stored
     * @throws IOException    exception raised following a missed or interrupted IO operation
     * @throws ParseException exception raised following an error in query parsing
     */
    public static void main(String[] args) throws IOException, ParseException {

        CranIndexer cranIndexer = new CranIndexer(DOCS_PATH, QUERIES_PATH);
        SearchEngine searchEngine = new SearchEngine(INDEX_PATH);

        searchEngine.open();

        cranIndexer.createDocs();
        File docsDir = new File(DOCS_PATH);
        File[] filesDoc = docsDir.listFiles();
        int i = 0;
        for (File file : Objects.requireNonNull(filesDoc)) {
            if (file.isFile() && !file.getPath().endsWith(".gitkeep")) {
                searchEngine.addDocument(cranIndexer.makeDoc(++i));
            }
        }

        searchEngine.close();

        cranIndexer.createQueries();

        File queryDir = new File(QUERIES_PATH);
        File[] filesQuery = queryDir.listFiles();
        FileWriter resultSet = new FileWriter(CRAN_PATH + File.separator + "results");
        int j = 1;
        while (j < Objects.requireNonNull(filesQuery).length) {
            CranQuery query = cranIndexer.makeQuery(j++);
            String result = "";
            String queryString = query.getAbstr();
            ArrayList<SearchResult> resultList = searchEngine.search(queryString);
            for (SearchResult currentResult : resultList) {
                resultSet.append(query.getID()).append(" 0 ").append(currentResult.getID());
                resultSet.append(" ").append(String.valueOf(currentResult.getRank())).append(" ");
                resultSet.append(String.valueOf(currentResult.getScore())).append(" exp_0").append("\n");
            }
            resultSet.write(result);
        }
        resultSet.close();
    }
}
