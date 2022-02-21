package catalina;

import http.HttpSession;

import java.util.Map;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 20:13
 * @Description: catalina
 * @Version: 1.0
 */
public class Context {

    private Host parent;

    private String path;
    private String docBase;
    private Boolean reloadable;

//    private WebappClassLoader classLoader;
//    private Map<String, ServletWrapper> servletWrapperMap;
//    private Map<String, FilterWrapper> filterWrapperMap;

    private Map<String, HttpSession> sessionMap;
    private Map<String, Object> attributes;


}
