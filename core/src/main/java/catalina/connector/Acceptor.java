package catalina.connector;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * 请求监听线程
 * @Autuor: innthehell
 * @Date: 2021/12/21 - 12 - 21 - 21:17
 * @Description: catalina.connector
 * @Version: 1.0
 */
@AllArgsConstructor
public class Acceptor implements Runnable {

    private final Endpoint endpoint;

    @Override
    public void run() {

        while (endpoint.isRunning()) {
            SocketChannel client;

            try {
                client = endpoint.accept();
                if (client == null) {
                    continue;
                }
                client.configureBlocking(false);
                // 注册到poller中的Selector中
                endpoint.registerToPoller(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
