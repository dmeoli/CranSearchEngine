package edu.uniba.donato.meoli.collection;

/**
 * La classe {@code CranDoc} modella un documento della collezione Cranfield.
 * @author Donato Meoli
 */
public class CranDoc {

    private String id;
    private String title;
    private String authors;
    private String department;
    private String abstr;

    /**
     * Costruisce un documento della collezione Cranfield.
     * @param id numero identificativo del documento
     * @param title titolo del documento
     * @param authors autore/i del documento
     * @param department nome del dipartimento addetto alla stesura del documento
     * @param abstr abstract del documento
     */
    public CranDoc(String id, String title, String authors, String department, String abstr) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.department = department;
        this.abstr = abstr;
    }

    /**
     * Restituisce il numero identificativo del documento.
     * @return numero identificativo del documento
     */
    public String getID() {
        return id;
    }

    /**
     * Restituisce il titolo del documento.
     * @return titolo del documento
     */
    public String getTitle() {
        return title;
    }

    /**
     * Restituisce l'autore/gli autori del documento.
     * @return autore/i del documento
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Restituisce il nome del dipartimento addetto alla stesura del documento.
     * @return dipartimento addetto alla stesura del documento
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Restituisce l'abstract del documento.
     * @return abstract del documento
     */
    public String getAbstr() {
        return abstr;
    }
}
