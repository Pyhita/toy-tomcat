package catalina.connector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/21 - 12 - 21 - 21:17
 * @Description: catalina.connector
 * @Version: 1.0
 */
@Data
@Slf4j
public class Endpoint {

    private Http11NioProtocol parent;

    // 请求处理线程池
    private Executor executor;

    // 监听线程，只有一个acceptor线程
    private Acceptor acceptor;

    // poller list，多个poller线程
    private List<Poller> pollerList;

    private volatile boolean isRunning;

    private ServerSocketChannel serverSocketChannel;

    // Poller轮询指针
    private AtomicInteger pollerIndex = new AtomicInteger(0);


    public SocketChannel accept() throws IOException {
        return serverSocketChannel.accept();
    }


    public void registerToPoller(SocketChannel client) throws IOException {
        serverSocketChannel.configureBlocking(false);
        getPoller().register(client, true);
        serverSocketChannel.configureBlocking(true);
    }

    /**
     * 采用轮询的方式，遍历PollerList中的每一个Poller
     * @return
     */
    private Poller getPoller() {
        int index = Math.abs(pollerIndex.incrementAndGet()) % pollerList.size();
        return pollerList.get(index);
    }


//    public boolean setSocketOptions(SocketChannel socket) {
//        try {
//            NioSocketWrapper socketWrapper = null;
//            NioSocketWrapper newWrapper = new NioSocketWrapper(this, socket);
//
//            socketWrapper = newWrapper;
//
//            socket.configureBlocking(false);
//            // 将socketWrapper注册到Poller中
//            getPoller().register();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

//    @AllArgsConstructor
//    public class NioSocketWrapper {
////        private final Poller poller;
//        private final Endpoint endpoint;
//        private final SocketChannel channel;
//
//
//
//    }

    public void execute(SocketProcessor processor) {
        executor.execute(processor);
    }

    public Http11ConnectionHandler getConnectionHandler() {
        return parent.getConnectionHandler();
    }

}
