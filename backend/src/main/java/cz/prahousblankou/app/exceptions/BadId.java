package cz.prahousblankou.app.exceptions;

public class BadId extends Exception {
    public BadId(String errorMessage) {
        super(errorMessage);
    }
}
