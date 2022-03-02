package catalina.connector;

import http.Request;
import http.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * @Autuor: innthehell
 * @Date: 2021/12/21 - 12 - 21 - 21:28
 * @Description: catalina.connector
 * @Version: 1.0
 */
@AllArgsConstructor
@Data
@Slf4j
public class Http11NioProcessor {

    private final CoyoteAdapter coyoteAdapter;
    private Request request;
    private Response response;
    private volatile boolean isFinished;
    private SocketProcessor socketProcessor;

    public Http11NioProcessor(SocketProcessor socketProcessor, CoyoteAdapter coyoteAdapter) {
        this.socketProcessor = socketProcessor;
        this.coyoteAdapter = coyoteAdapter;
    }


    public synchronized void process() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            isFinished = false;
            int len = 0;
            while ((len = socketProcessor.read(byteBuffer)) != -1) {
                // 翻转读写
                byteBuffer.flip();
                bos.write(byteBuffer.array(), 0, len);
                byteBuffer.clear();
            }

            // 所有的请求数据已经读入了bos中
            bos.close();
            if (bos == null) return;

            byte[] bytes = bos.toByteArray();
            this.request = new Request(bytes, this);
            this.response = new Response();

            log.info(request.toString());
            // 将解析好的request和response分发给对应的容器进行处理
            // 通过coyoteAdapter发送给合适的容器处理
            isFinished = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



}
