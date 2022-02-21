package catalina;

import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 21:07
 * @Description: catalina
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public abstract class Wrapper {

    protected String name;
    protected String className;
    protected String urlPattern;
    protected Map<String, String> initParamMap;
    protected Context parent;

    protected Object obj;

    public Wrapper(Context parent,String name, String className, Map<String, String> initParamMap) {
        this.parent = parent;
        this.name = name;
        this.className = className;
        this.initParamMap = initParamMap;
    }


    public synchronized void load() throws Exception {
//        WebappClassLoader classLoader = parent.getClassLoader();
//        Class<?> aClass = classLoader.loadClass(className);
//        obj = ReflectUtil.newInstance(aClass);
    }



}
