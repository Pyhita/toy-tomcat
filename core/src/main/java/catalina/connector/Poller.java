package catalina.connector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/21 - 12 - 21 - 21:25
 * @Description: catalina.connector
 * @Version: 1.0
 */
@Data
@Slf4j
public class Poller implements Runnable {
    private String name;
    private Endpoint endpoint;
    private CoyoteAdapter coyoteAdapter;

    private Map<SocketChannel, Http11NioProcessor> nioProcessorMap;
    private Map<SocketChannel, SocketProcessor> processorMap;

    // poller封装了nio里的Selector
    private Selector selector;
    private SynchronousQueue<PollerEvent> events;


    @Override
    public void run() {
        while (endpoint.isRunning()) {
            events();
            try {
                if (selector.select() <= 0) continue;

                Iterator<SelectionKey> iterator = selector.keys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isValid() && key.isReadable()) {
                        SocketProcessor processor = (SocketProcessor) key.attachment();
                        if (processor != null) {
                            // 将这个processor 提交给线程池进行处理
                            executeProcessor(processor);
                        }
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void executeProcessor(SocketProcessor processor) {
        // 开始工作
        processor.setWorking(true);
        endpoint.execute(processor);
    }

    /**
     * 遍历整个队列，取出每一个PollerEvent，调用它的run方法
     * @return
     */
    private boolean events() {
        boolean res = false;
        PollerEvent event;

        while (!events.isEmpty()) {
            event = events.poll();
            if (event == null) {
                break;
            }
            event.run();
            res = true;
        }

        return res;
    }

        // 将socketchannel注册到Poller中
    public void register(SocketChannel client, boolean isNewSocket) throws ClosedChannelException {
        SocketProcessor socketProcessor = null;
        if (isNewSocket) {
            socketProcessor = new SocketProcessor(endpoint, client, this, isNewSocket);
            Http11NioProcessor http11NioProcessor = new Http11NioProcessor(socketProcessor, coyoteAdapter);
            nioProcessorMap.put(client, http11NioProcessor);
            processorMap.put(client, socketProcessor);
        } else {
            socketProcessor = processorMap.get(client);
        }
        // 加入到PollerEvent队列中
        events.offer(new PollerEvent(socketProcessor));
//        client.register(selector, SelectionKey.OP_READ);
    }

    @Data
    @AllArgsConstructor
    private static class PollerEvent implements Runnable {
        private SocketProcessor socketProcessor;

        @Override
        public void run() {
            try {
                socketProcessor.getSocketChannel().register(
                        socketProcessor.getPoller().getSelector(), SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                log.error("Socket{} 已经被关闭，无法注册到 Poller", socketProcessor.getSocketChannel());
            }
        }
    }


}
