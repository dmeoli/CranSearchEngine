package edu.uniba.di.lacam.swap.donato.meoli.retrieval;

/**
 * The {@code SearchResult} class models a search result.
 *
 * @author Donato Meoli
 */
public class SearchResult {

    private final String id;
    private final float score;
    private final int rank;

    /**
     * Build a search result.
     *
     * @param id    identification number of the document
     * @param score score of the document
     * @param rank  document rank
     */
    public SearchResult(String id, float score, int rank) {
        this.id = id;
        this.score = score;
        this.rank = rank;
    }

    /**
     * Returns the identification number of the document.
     *
     * @return identification number of the document
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the score of the document.
     *
     * @return score of the document
     */
    public float getScore() {
        return score;
    }

    /**
     * Returns the rank of the document.
     *
     * @return document rank
     */
    public int getRank() {
        return rank;
    }
}