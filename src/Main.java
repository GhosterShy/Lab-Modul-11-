import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class  Book
{
    private String  Title;
    private String Author;
    private String ISBN;
    private boolean isAvailable;

    public Book(String title, String author, String ISBN) {
        Title = title;
        Author = author;
        this.ISBN = ISBN;
        this.isAvailable = true;
    }


    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getISBN() {
        return ISBN;
    }

    public boolean isAvailable() {
        return isAvailable;
    }



    public  void MarkAsLoaned() {isAvailable = true;}

    public void  MarkAsAvailable() {isAvailable = false;}


}

class Reader
{
    private  int id;
    private String Name;
    private  String Email;


    public Reader(int id, String name, String email) {
        this.id = id;
        this.Name = name;
        this.Email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }





    public boolean BorrowBook(Book book)
    {
        if(book.isAvailable())
        {
            book.MarkAsLoaned();
            System.out.println(getName() + "взял книгу" + book.getTitle());

        }
        return true;
    }

    public void  ReturnBook(Book book) {
        book.MarkAsAvailable();
        System.out.println(getName() + "возвращал книгу" + book.getTitle());
    }

}


class  Librarian
{
    private  int id;
    private  String Name;
    private  String Position;
    private List<Book> books = new ArrayList<>();

    public Librarian(int id, String name, String position) {
        this.id = id;
        Name = name;
        Position = position;
    }

    public void  AddBook(Book book) {
        books.add(book);
        System.out.println(book.getTitle() + "добавилась" + Name);
    }
    public void RemoveBook(Book book){
        books.remove(book);
        System.out.println(book.getTitle() + "удалилась из " + Name);
    }
    public void GenerateReport() {
        System.out.println("Report");
        for (Book book:books)
        {
            System.out.println(book.getTitle() + " " + book.isAvailable());
        }
    }


}


class Loan
{
    private Book book;
    private Reader reader;
    private Date loanDate;
    private Date ReturnDate;


    public Loan(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
        this.loanDate = new Date();
    }


    public boolean IssueLoan(){
        if (book.isAvailable()) {
            book.MarkAsLoaned();
            System.out.println("Loan issued for " + book.getTitle() + " to " + reader.getName());
            return true;
        } else {
            System.out.println("The book " + book.getTitle() + " is not available for loan.");
            return false;
        }
    }

    public void CompleteLoan(){
        book.MarkAsAvailable();
        this.ReturnDate = new Date();
        System.out.println("Loan completed for " + book.getTitle() + " returned by " + reader.getName());
    }
}








public class Main {
    public static void main(String[] args) {
        Librarian librarian = new Librarian(1, " Library ", "Head Librarian");


        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "123456789");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "987654321");
        librarian.AddBook(book1);
        librarian.AddBook(book2);


        Reader reader = new Reader(1, "Shyngys ", "Shyngys@example.com");

        reader.BorrowBook(book1);


        Loan loan = new Loan(book1, reader);
        loan.IssueLoan();


        loan.CompleteLoan();
        reader.ReturnBook(book1);


        System.out.println("");
        librarian.GenerateReport();


    }
}


