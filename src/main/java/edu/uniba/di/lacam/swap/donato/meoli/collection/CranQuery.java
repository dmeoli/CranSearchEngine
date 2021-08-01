package edu.uniba.di.lacam.swap.donato.meoli.collection;

/**
 * The {@code CranQuery} class models a query for the Cranfield collection.
 *
 * @author Donato Meoli
 */
public class CranQuery {

    private final String id;
    private final String abstr;

    /**
     * Build a query for the Cranfield collection.
     *
     * @param id    query identification number
     * @param abstr abstract della query
     */
    public CranQuery(String id, String abstr) {
        this.id = id;
        this.abstr = abstr;
    }

    /**
     * Returns the query identification number.
     *
     * @return query identification number
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the abstract of the query.
     *
     * @return query abstract
     */
    public String getAbstr() {
        return abstr;
    }
}
