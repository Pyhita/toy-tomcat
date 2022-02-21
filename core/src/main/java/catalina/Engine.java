package catalina;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 20:25
 * @Description: catalina
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
public class Engine {

    private Service parent;
    private String name;
    private Host defaultHost;
    private List<Host> hosts;

    public Engine(Service parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public void start() throws Exception {
        for (Host host : hosts) {
//            host.start();
        }
    }

    public void shutdown() {
        for (Host host : hosts) {
//            host.shutdown();
        }
    }

    @Override
    public String toString() {
        return "Engine{" +
                "name='" + name + '\'' +
                '}';
    }





}
