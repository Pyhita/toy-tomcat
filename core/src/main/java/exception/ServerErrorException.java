package exception;

import util.HttpStatus;

/**
 * @Author: pyhita
 * @Date: 2022/3/2
 * @Descrption: exception
 * @Version: 1.0
 */
public class ServerErrorException extends ServletException {
    public ServerErrorException(String messages) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, messages);
    }

    @Override
    public String toString() {
        return "ServerErrorException{" +
                "status=" + status +
                ", messages='" + messages + '\'' +
                '}';
    }
}
