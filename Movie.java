
public class Movie {
    private int id;
    private String title;
    private String showtime;
    private double price;

    public Movie(int id, String title, String showtime, double price) {
        this.id = id;
        this.title = title;
        this.showtime = showtime;
        this.price = price;
    }

    public String toLine() {
        return id + "|" + title + "|" + showtime + "|" + price;
    }
}
