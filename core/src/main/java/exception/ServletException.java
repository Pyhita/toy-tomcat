package exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.HttpStatus;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 21:11
 * @Description: exception
 * @Version: 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServletException extends Exception {
    protected HttpStatus status;
    protected String messages;

    @Override
    public String toString() {
        return "ServletException{" +
                "status=" + status +
                ", messages='" + messages + '\'' +
                '}';
    }


}
