
public class Booking {
    private int bookingId;
    private int movieId;
    private String title;
    private String showtime;
    private double price;
    private int quantity;
    private String status;

    public Booking(int bookingId, int movieId, String title, String showtime, double price, int quantity, String status) {
        this.bookingId = bookingId;
        this.movieId = movieId;
        this.title = title;
        this.showtime = showtime;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public String toLine() {
        return bookingId + "|" + movieId + "|" + title + "|" + showtime + "|" + price + "|" + quantity + "|" + status;
    }
}