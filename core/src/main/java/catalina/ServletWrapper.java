package catalina;

import exception.ServletException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import servlet.Servlet;

import java.util.Map;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 21:03
 * @Description: catalina
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServletWrapper extends Wrapper {

    private Integer loadOnStartup;

    public ServletWrapper(Context parent, String servletName, String servletClass, Map<String, String> initParamMap, Integer loadOnStartup) {
        super(parent, servletName, servletClass, initParamMap);
        this.loadOnStartup = loadOnStartup;
    }

    public synchronized void load() throws Exception {
        if (obj != null) {
            return;
        }
        super.load();
        ((Servlet) obj).init();
    }

    public synchronized void unLoad() throws ServletException {
        if (obj == null) {
            return;
        }
        ((Servlet) obj).destroy();
    }

    public Servlet getServlet() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Servlet) obj;
    }

}
