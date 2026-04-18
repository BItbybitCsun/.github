/*
    Module/Class Name: User.java
    Date of Code: 03/15/26
    Programmer: Tony Ashvanian

    Brief Description: The User.java class is a model class that stores
    a registered user's credentials. It is used by LoginHandler to compare
    stored credentials against a login attempt.

    Important Functions:

    User(String username, String password)
    Input: username and password strings
    Output: A new User object
    Purpose: Constructs a User with the given credentials loaded from users.txt.

    checkLogin(String u, String p)
    Input: Username and password to verify
    Output: Boolean - true if credentials match, false otherwise
    Purpose: Compares the provided credentials to the stored username and password.

    getUsername() / getPassword()
    Input: None
    Output: The stored username or password string
    Purpose: Accessor methods for retrieving user credential fields.

    Important Data Structures: Two private String fields (username, password)
    representing the user's stored credentials.

    Algorithm/Design Used: Simple equality check for credential validation.
    User objects are instantiated per line read from users.txt and checked
    one at a time (linear search performed in LoginHandler).
*/

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkLogin(String u, String p) {
        return username.equals(u) && password.equals(p);
    }
}