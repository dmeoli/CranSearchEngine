package edu.uniba.dib.donato.meoli.retrieval;

/**
 * La classe {@code SearchResult} modella un risultato della ricerca.
 * @author Donato Meoli
 */
public class SearchResult {

    private String id;
    private float score;
    private int rank;

    /**
     * Costruisce un risultato della ricerca.
     * @param id numero identificativo del documento
     * @param score score del documento
     * @param rank rank del documento
     */
    public SearchResult(String id, float score, int rank) {
        this.id = id;
        this.score = score;
        this. rank = rank;
    }

    /**
     * Restituisce il numero identificativo del documento.
     * @return numero identificativo del documento
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce lo score del documento.
     * @return score del documento
     */
    public float getScore() {
        return score;
    }

    /**
     * Restituisce il rank del documento.
     * @return rank del documento
     */
    public int getRank() {
        return rank;
    }
}