import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ektorp.support.CouchDbDocument;

@JsonSerialize
public class Book extends CouchDbDocument {
    public String title, year, category;
    //private String id, revision;

    @Override
    public String toString() {
        return "ID: " + getId() + ", tytul: " + title + ", rok: " + year + ", kategoria: " + category;
    }
}
