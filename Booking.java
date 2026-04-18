/*
Module/Class Name: Booking.java
Date of Code: 02/29/26
Programmer: Esmeralda Munoz
Brief Description: The Booking.java class creates booking objects 
with each movie that is ordered by a user.

Important Functions:
Booking(...)
Input: booking id, movie id, title, showtime, price, quantity, 
and status
Output: Creates a booking object

toLine()
Input: None
Output: Returns the string form of the booking object

Important Data Structures: booking id, movie id, movie title, showtime,
price, quantity, and status. 

Algorithm/Design Used: The booking object’s data is serialized using the
pipe character “|” to separate the attributes of each booking object.
*/


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