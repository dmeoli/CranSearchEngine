package com.donato;

import com.donato.collection.CranQuery;
import com.donato.retrieval.CranIndexer;
import com.donato.retrieval.SearchEngine;
import com.donato.retrieval.SearchResult;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * La classe {@code CranSearcher} fornisce i risultati della ricerca delle 225 query sui 1400 documenti della collezione
 * Cranfield.
 * @author Donato Meoli
 */
public class CranSearcher {

    /**
     * Funzione main per il calcolo dei risultati delle 225 query sui 1400 documenti della collezione Cranfield.
     * @param args array di stringhe in cui vengono memorizzati i parametri passati al programma
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     * @throws ParseException eccezione sollevata in seguito ad un errore nel parsing della query
     */
    public static void main(String[] args) throws IOException, ParseException {

        final String currentDirPath = System.getProperty("user.dir");
        final String collectionPath = currentDirPath + "/cran/collection";
        final String docsPath = currentDirPath + "/cran/docs";
        final String indexPath = currentDirPath + "/cran/index";
        final String queriesPath = currentDirPath + "/cran/queries";
        final String resultPath = currentDirPath + "/cran";

        CranIndexer cranIndexer = new CranIndexer(collectionPath, docsPath, queriesPath);
        SearchEngine searchEngine = new SearchEngine(indexPath, resultPath);

        searchEngine.open();

        cranIndexer.createDocs();
        File docsDir = new File(docsPath);
        File[] filesDoc = docsDir.listFiles();
        int i = 0;
        for (File file: filesDoc) {
            if (file.isFile() && !file.getPath().endsWith(".gitkeep")) {
                searchEngine.addDocument(cranIndexer.makeDoc(++i));
            }
        }

        searchEngine.close();

        cranIndexer.createQueries();

        File queryDir = new File(queriesPath);
        File[] filesQuery = queryDir.listFiles();
        FileWriter resultSet = new FileWriter(resultPath + "/results");
        int j = 1;
        while (j < filesQuery.length) {
            CranQuery query = cranIndexer.makeQuery(j++);
            String result = "";
            String queryString = query.getAbstr();
            ArrayList<SearchResult> resultList = searchEngine.search(queryString);
            for (SearchResult currentResult: resultList) {
                resultSet.append(query.getID()).append(" 0 ").append(currentResult.getID());
                resultSet.append(" ").append(String.valueOf(currentResult.getRank())).append(" ");
                resultSet.append(String.valueOf(currentResult.getScore())).append(" exp_0\n");
            }
            resultSet.write(result);
        }
        resultSet.close();
    }
}
