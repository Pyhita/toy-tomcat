package exception;


import util.HttpStatus;

public class BadRequestException extends ServletException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST,message);
    }

    @Override
    public String toString() {
        return "BadRequestException{" +
                "status=" + status +
                ", messages='" + messages + '\'' +
                '}';
    }
}
