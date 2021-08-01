package edu.uniba.di.lacam.swap.donato.meoli.retrieval;

import edu.uniba.di.lacam.swap.donato.meoli.collection.CranDoc;
import edu.uniba.di.lacam.swap.donato.meoli.collection.CranQuery;

import java.io.*;
import java.util.Objects;

/**
 * The {@code Indexer} class models the inverted index.
 *
 * @author Donato Meoli
 */
public class CranIndexer {

    private final String docsPath;
    private final String queriesPath;

    /**
     * Constructs the inverted index.
     *
     * @param docsPath    path to the folder containing the 1400 documents of the Cranfield collection
     * @param queriesPath path to the folder containing the 225 queries for the Cranfield collection
     */
    public CranIndexer(String docsPath, String queriesPath) {
        this.docsPath = docsPath;
        this.queriesPath = queriesPath;
    }

    /**
     * Create the 1400 documents from the Cranfield document collection.
     *
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public void createDocs() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(File.separator + "cran.all.1400")));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String doc = bufferedReader.readLine();
        int i = 1;
        while (doc != null) {
            FileWriter fileWriter = new FileWriter(docsPath + File.separator + "cran.doc." + i);
            fileWriter.append(doc).append("\n");
            doc = bufferedReader.readLine();
            while (doc != null && !doc.startsWith(".I")) {
                fileWriter.append(doc).append("\n");
                doc = bufferedReader.readLine();
            }
            i++;
            fileWriter.close();
        }
        bufferedReader.close();
    }

    /**
     * Build the 225 queries from the Cranfield query collection.
     *
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public void createQueries() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(File.separator + "cran.qry")));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String query = bufferedReader.readLine();
        int i = 1;
        while (query != null) {
            FileWriter fileWriter = new FileWriter(queriesPath + File.separator + "cran.qry." + i);
            fileWriter.append(query).append("\n");
            query = bufferedReader.readLine();
            while (query != null && !query.startsWith(".I")) {
                fileWriter.append(query).append("\n");
                query = bufferedReader.readLine();
            }
            i++;
            fileWriter.close();
        }
        bufferedReader.close();
    }

    /**
     * Creates an object {@code CranDoc} starting from the Cranfield
     * collection document identified by the specified id.
     *
     * @param id identification number of the document
     * @return object {@code CranDoc} representing the document identified by the specified id
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public CranDoc makeDoc(int id) throws IOException {
        FileReader fileReader = new FileReader(new File(docsPath + File.separator + "cran.doc." + id));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String doc = bufferedReader.readLine();
        doc = bufferedReader.readLine();
        StringBuilder title = new StringBuilder();
        StringBuilder authors = new StringBuilder();
        StringBuilder department = new StringBuilder();
        StringBuilder abstr = new StringBuilder();
        while (doc != null) {
            switch (doc) {
                case ".T":
                    doc = bufferedReader.readLine();
                    while (!doc.equals(".A")) {
                        title.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                case ".A":
                    doc = bufferedReader.readLine();
                    while (!doc.equals(".B")) {
                        authors.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                case ".B":
                    doc = bufferedReader.readLine();
                    while (!doc.equals(".W")) {
                        department.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                case ".W":
                    doc = bufferedReader.readLine();
                    while (doc != null) {
                        abstr.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                default:
                    break;
            }
        }
        bufferedReader.close();
        String currentId = Integer.toString(id);
        return new CranDoc(currentId, title.toString(), authors.toString(), department.toString(), abstr.toString());
    }

    /**
     * Creates a {@code CranQuery} object starting from the Cranfield
     * collection query identified by the specified id.
     *
     * @param id query identification number
     * @return {@code CranQuery} object representing the query identified by the specified id
     * @throws IOException exception raised following a missed or interrupted IO operation
     */
    public CranQuery makeQuery(int id) throws IOException {
        FileReader fileReader = new FileReader(new File(queriesPath + File.separator + "cran.qry." + id));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String query = bufferedReader.readLine();
        query = bufferedReader.readLine();
        query = bufferedReader.readLine();
        StringBuilder abstr = new StringBuilder();
        while (query != null) {
            abstr.append(query).append("\n");
            query = bufferedReader.readLine();
        }
        bufferedReader.close();
        String currentId = Integer.toString(id);
        return new CranQuery(currentId, abstr.toString());
    }
}
