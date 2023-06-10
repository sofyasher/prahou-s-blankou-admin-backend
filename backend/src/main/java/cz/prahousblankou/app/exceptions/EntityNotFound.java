package cz.prahousblankou.app.exceptions;

public class EntityNotFound extends Exception {
    public EntityNotFound(String errorMessage) {
        super(errorMessage);
    }
}
