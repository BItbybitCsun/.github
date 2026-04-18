/*Module/Class Name: Movie.java
Date of Code: February 25
Programmer: Gor
Brief Description:
The Movie.java class defines each movie that is available for purchase in the ByteBook system.
Important Functions:
Movie(int id, String title, String showtime, double price)
Input: Movie id, title, showtime, and ticket price
Output: Creates a movie object
toLine()
Input: None
Output: Returns the string form of the movie object
Important Data Structures:
Each movie object has the following attributes: id, title, showtime, and price
Algorithm/Design Used:
Using the toString() method to return the movie as a string object allows the data to be
serialized and sent from the server to the client application. */


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
