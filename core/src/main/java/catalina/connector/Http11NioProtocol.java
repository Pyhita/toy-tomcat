package catalina.connector;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/21 - 12 - 21 - 21:23
 * @Description: catalina.connector
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
public class Http11NioProtocol {

    private Http11ConnectionHandler connectionHandler;



}
