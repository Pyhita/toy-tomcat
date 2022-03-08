package catalina;

import catalina.connector.Connector;
import lombok.Data;

import java.util.List;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 20:26
 * @Description: catalina
 * @Version: 1.0
 */
@Data
public class Service {
    private String name;
    private Engine engine;
    private List<Connector> connectors;
    private Server parent;

    public Service(Server parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    // 启动其下面的子容器
    public void start() throws Exception {
        // 启动service下面的所有连接器
        for (Connector connector : connectors) {
            connector.start();
        }
        engine.start();
    }

    public void shutdown() throws Exception {
        for (Connector connector : connectors) {
            connector.shutdown();
        }
        engine.shutdown();
    }




}
