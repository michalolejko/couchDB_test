import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

//https://helun.github.io/Ektorp/reference_documentation.html
//https://www.weaii-moodle.tu.kielce.pl/mod/page/view.php?id=9114
//http://localhost:5984/_utils/#

public class Main {

    static HttpClient httpClient;
    static CouchDbConnector db;
    static Scanner scanner;
    static int id;

    public static void main(String[] args) throws MalformedURLException {

        //1.
        httpClient = new StdHttpClient.Builder()
                .username("admin")
                .password("admin")
                .url("http://localhost:5984")
                .build();
        //2.
        db = new StdCouchDbConnector("library", new StdCouchDbInstance(httpClient));
        db.createDatabaseIfNotExists();

        //3.
        scanner = new Scanner(System.in);
        while (true) {
            showAllRecords();
            pressEnter();
            showMenu();
            switch (scanner.nextInt()) {
                case 0:
                    System.out.println("Zakonczono");
                    return;
                case 1:
                    saveRecord();
                    break;
                case 2:
                    updateRecord();
                    break;
                case 3:
                    deleteRecord();
                    break;
                case 4:
                    getRecordById();
                    break;
                case 5:
                    getRecordByTitle();
                    break;
                case 6:
                    processing();
                    break;
            }
        }
    }

    static void showMenu() {
        System.out.print("\n2) Biblioteka (CouchDB)\n\nWybierz operacje:\n" +
                "1.Zapisywanie\n2.Aktualizowanie\n3.Kasowanie\n4.Pobieranie po ID\n5. Pobieranie po tytule\n" +
                "6.Przetwarzanie(po kategorii)\n0.Zakoncz\n\nWpisz cyfre i zatwierdz enterem: ");
    }

    private static void processing() {
        scanner.nextLine();
        System.out.println("Przetwarzanie polega na usunieciu wszystkich ksiazek z podanej przez Ciebie kategorii: ");
        String cat = scanner.nextLine();
        for (String id : db.getAllDocIds())
            if(db.get(Book.class, id).category.equals(cat))
                db.delete(db.get(Book.class,id));
    }

    private static void getRecordByTitle() {
        scanner.nextLine();
        System.out.println("Podaj tytul:");
        showAllRecords();
        String title = scanner.nextLine();
        for (String id : db.getAllDocIds())
            if(db.get(Book.class, id).title.equals(title))
                System.out.println(db.get(Book.class,id));
    }

    private static void getRecordById() {
        scanner.nextLine();
        System.out.println("Wybierz id:");
        showAllRecords();
        String id = scanner.nextLine();
        System.out.println(db.get(Book.class, id));
    }

    private static void deleteRecord() {
        scanner.nextLine();
        System.out.println("Wybierz id:");
        showAllRecords();
        String id = scanner.nextLine();
        Book newBook = db.get(Book.class, id);
        db.delete(newBook);
        System.out.println("Usunieto " + newBook);
    }

    private static void updateRecord() {
        scanner.nextLine();
        System.out.println("Wybierz id:");
        showAllRecords();
        String id = scanner.nextLine();
        Book newBook = db.get(Book.class, id);
        System.out.println("Podaj tytul: ");
        newBook.title = scanner.nextLine();
        System.out.println("Podaj rok:");
        newBook.year = scanner.nextLine();
        System.out.println("Podaj kategorie:");
        newBook.category = scanner.nextLine();
        db.update(newBook);
        System.out.println("Zaktualizowano ksiazke: " + newBook);
    }

    private static void saveRecord() {
        scanner.nextLine();
        Book newBook = new Book();
        System.out.println("Podaj tytul: ");
        newBook.title = scanner.nextLine();
        System.out.println("Podaj rok:");
        newBook.year = scanner.nextLine();
        System.out.println("Podaj kategorie:");
        newBook.category = scanner.nextLine();
        db.create(newBook);
        System.out.println("Stworzono ksiazke: " + newBook);
    }

    private static void showAllRecords() {
        System.out.println("\nWszystkie rekordy w bazie:");
        for (String id : db.getAllDocIds())
            System.out.println(db.get(Book.class, id));
    }
    private static void pressEnter() {
        System.out.println("Wcisnij enter, aby kontynuowac...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
