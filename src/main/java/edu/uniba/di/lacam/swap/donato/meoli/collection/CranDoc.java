package edu.uniba.di.lacam.swap.donato.meoli.collection;

/**
 * The {@code CranDoc} class models a document from the Cranfield collection.
 *
 * @author Donato Meoli
 */
public class CranDoc {

    private final String id;
    private final String title;
    private final String authors;
    private final String department;
    private final String abstr;

    /**
     * Constructs a document from the Cranfield collection.
     *
     * @param id         identification number of the document
     * @param title      title of the document
     * @param authors    authors of the document
     * @param department name of the department responsible for drafting the document
     * @param abstr      document abstract
     */
    public CranDoc(String id, String title, String authors, String department, String abstr) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.department = department;
        this.abstr = abstr;
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
     * Returns the title of the document.
     *
     * @return title of the document
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author(s) of the document.
     *
     * @return author(s) of the document
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Returns the name of the department responsible for drafting the document.
     *
     * @return department responsible for drafting the document
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the abstract of the document.
     *
     * @return document abstract
     */
    public String getAbstr() {
        return abstr;
    }
}
