package exception;

import util.HttpStatus;

/**
 * @Author: pyhita
 * @Date: 2022/3/2
 * @Descrption: exception
 * @Version: 1.0
 */
public class ResourceNotFoundException extends ServletException {
    public ResourceNotFoundException(String messages) {
        super(HttpStatus.NOT_FOUND, messages);
    }

    @Override
    public String toString() {
        return "ResourceNotFoundException{" +
                "status=" + status +
                ", messages='" + messages + '\'' +
                '}';
    }
}
