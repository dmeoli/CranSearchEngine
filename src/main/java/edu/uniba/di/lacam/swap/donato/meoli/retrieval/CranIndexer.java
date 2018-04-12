package edu.uniba.di.lacam.swap.donato.meoli.retrieval;

import edu.uniba.di.lacam.swap.donato.meoli.collection.CranDoc;
import edu.uniba.di.lacam.swap.donato.meoli.collection.CranQuery;

import java.io.*;

/**
 * La classe {@code Indexer} modella l'indice invertito.
 * @author Donato Meoli
 */
public class CranIndexer {

    private String collectionPath;
    private String docsPath;
    private String queriesPath;

    /**
     * Costruisce l'indice invertito.
     * @param collectionPath percorso della cartella contenente la collezione di documenti e di query Cranfield.
     * @param docsPath percorso della cartella contenente i 1400 documenti della collezione Cranfield
     * @param queriesPath percorso della cartella contenente le 225 query per la collezione Cranfield
     */
    public CranIndexer(String collectionPath, String docsPath, String queriesPath) {
        this.collectionPath = collectionPath;
        this.docsPath = docsPath;
        this.queriesPath = queriesPath;
    }

    /**
     * Crea i 1400 documenti a partire dalla collezione di documenti Cranfield.
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public void createDocs() throws IOException {
        FileReader fileReader = new FileReader(new File(collectionPath + "/cran.all.1400"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String doc = bufferedReader.readLine();
        int i = 1;
        while (doc != null) {
            FileWriter fileWriter = new FileWriter(docsPath + "/cran.doc." + i);
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
     * Crea le 225 query a partire dalla collezione di query Cranfield.
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public void createQueries() throws IOException {
        FileReader fileReader = new FileReader(new File(collectionPath + "/cran.qry"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String query = bufferedReader.readLine();
        int i = 1;
        while (query != null) {
            FileWriter fileWriter = new FileWriter(queriesPath + "/cran.qry." + i);
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
     * Crea un oggetto {@code CranDoc} a partire dal documento della
     * collezione Cranfield identificato dall'id specificato.
     * @param id numero identificativo del documento
     * @return oggetto {@code CranDoc} rappresentante il documento identificato dall'id specificato
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public CranDoc makeDoc(int id) throws IOException {
        FileReader fileReader = new FileReader(new File(docsPath + "/cran.doc." + id));
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
     * Crea un oggetto {@code CranQuery} a partire dalla query della
     * collezione Cranfield identificata dall'id specificato.
     * @param id numero identificativo della query
     * @return oggetto {@code CranQuery} rappresentante la query identificata dall'id specificato
     * @throws IOException eccezione sollevata in seguito ad una mancata o interrotta operazione di I/O
     */
    public CranQuery makeQuery(int id) throws IOException {
        FileReader fileReader = new FileReader(new File(queriesPath + "/cran.qry." + id));
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
