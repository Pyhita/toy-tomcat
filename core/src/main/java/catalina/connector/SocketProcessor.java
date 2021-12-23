package catalina.connector;

import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/21 - 12 - 21 - 21:28
 * @Description: catalina.connector
 * @Version: 1.0
 */
@Data
public class SocketProcessor implements Runnable {
    private final Endpoint endpoint;
    private SocketChannel socketChannel;
    private Poller poller;
    private final boolean isNewSocket;
    private volatile long waitBegin;
    private volatile boolean isWorking;


    public SocketProcessor(Endpoint endpoint,
                           SocketChannel socketChannel,
                           Poller poller,
                           boolean isNewSocket) {
        this.endpoint = endpoint;
        this.socketChannel = socketChannel;
        this.poller = poller;
        this.isNewSocket = isNewSocket;
    }

    @Override
    public void run() {
        isWorking = true;
        getConnectionHandler().process(this);
        isWorking = false;

    }

    public Http11ConnectionHandler getConnectionHandler() {
        return endpoint.getConnectionHandler();
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        int len = 0;
        len = socketChannel.read(byteBuffer);

        return len;
    }


}
