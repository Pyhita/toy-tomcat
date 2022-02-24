package catalina;

import exception.ServletException;
import filter.Filter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @Author: pyhita
 * @Date: 2022/2/24
 * @Descrption: catalina
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FilterWrapper extends Wrapper {
    public FilterWrapper(Context parent, String filterName, String filterClass, Map<String, String> initParamMap) {
        super(parent, filterName, filterClass, initParamMap);
    }

    public synchronized void load() throws Exception {
        if (obj != null) {
            return;
        }
        super.load();
        ((Filter) obj).init();
    }

    public synchronized void unLoad() throws ServletException {
        if (obj == null) {
            return;
        }
        ((Filter) obj).destroy();
    }

    public Filter getFilter() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Filter) obj;
    }
}