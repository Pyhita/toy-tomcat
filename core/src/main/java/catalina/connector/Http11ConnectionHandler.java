package catalina.connector;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/22 - 12 - 22 - 22:08
 * @Description: catalina.connector
 * @Version: 1.0
 */
@AllArgsConstructor
@Slf4j
public class Http11ConnectionHandler implements Runnable {

    private Map<SocketChannel, Http11NioProcessor> nioProcessorMap;
    private Map<SocketChannel, SocketProcessor> socketProcessorMap;


    public void process(SocketProcessor socketProcessor) {
        Http11NioProcessor nioProcessor = nioProcessorMap.get(socketProcessor.getSocketChannel());
        if (nioProcessor == null) {
            return;
        }
        nioProcessor.process();
    }

    @Override
    public void run() {

    }
}
