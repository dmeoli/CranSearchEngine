package edu.uniba.di.lacam.swap.donato.meoli.collection;

/**
 * La classe {@code CranQuery} modella una query per la collezione Cranfield.
 *
 * @author Donato Meoli
 */
public class CranQuery {

    private String id;
    private String abstr;

    /**
     * Costruisce una query per la collezione Cranfield.
     * @param id numero identificativo della query
     * @param abstr abstract della query
     */
    public CranQuery(String id, String abstr) {
        this.id = id;
        this.abstr = abstr;
    }

    /**
     * Restituisce il numero identificativo della query.
     * @return numero identificativo della query
     */
    public String getID() {
        return id;
    }

    /**
     * Restituisce l'abstract della query.
     * @return abstract della query
     */
    public String getAbstr() {
        return abstr;
    }
}
