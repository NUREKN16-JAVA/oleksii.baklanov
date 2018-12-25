package baklanov.usermanagement.agent;

public class SearchException extends Exception {

    public SearchException(Throwable cause) {
        super(cause);
    }

    public SearchException(String e) {
        super(e);
    }
}