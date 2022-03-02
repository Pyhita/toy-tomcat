package catalina;

import classLoader.WebappClassLoader;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import exception.BadRequestException;
import exception.ServletException;
import filter.Filter;
import http.Cookie;
import http.HttpSession;
import http.Response;
import lombok.Data;
import util.Constant;
import util.XmlUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 20:13
 * @Description: catalina
 * @Version: 1.0
 */
@Data
public class Context {

    private Host parent;

    private String path;
    private String docBase;
    private Boolean reloadable;

    private WebappClassLoader classLoader;
    private Map<String, ServletWrapper> servletWrapperMap;
    private Map<String, FilterWrapper> filterWrapperMap;

    private Map<String, HttpSession> sessionMap;
    private Map<String, Object> attributes;

    public Context(Host parent, String path, String docBase, Boolean reloadable) {
        this.parent = parent;
        this.path = path;
        this.docBase = docBase;
        this.reloadable = reloadable;

        this.filterWrapperMap = new ConcurrentHashMap<>();
        this.servletWrapperMap = new ConcurrentHashMap<>();
        this.sessionMap = new ConcurrentHashMap<>();
        this.attributes = new ConcurrentHashMap<>();
    }

    public void init() throws Exception {
        // 设置类路径，创建类加载器
        createClassLoader();
        // 读取xml文件，设置servlet和路径和映射关系
        createWrapper();
        // 是否设置了启动时便加载
        loadOnStartup();
    }

    private void createClassLoader() {
        // classPath：servlet类路径
        // libClassPath：类库路径
        File classPath = new File(docBase, Constant.WEB_CLASSES);
        File libClassPath = new File(docBase, Constant.WEB_LIB_CLASSES);
        List<URL> urlList = new ArrayList<>();
        try {
            urlList.add(classPath.toURI().toURL());
            List<File> libFiles = FileUtil.loopFiles(libClassPath);
            for (File file : libFiles) {
                urlList.add(file.toURI().toURL());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL[] urls = urlList.toArray(new URL[0]);
        classLoader = new WebappClassLoader(urls, Context.class.getClassLoader());
    }

    private void loadOnStartup() throws Exception {
        Collection<ServletWrapper> servletWrappers = servletWrapperMap.values();

        TreeMap<Integer, ArrayList<ServletWrapper>> map = new TreeMap<>();
        for (ServletWrapper servletWrapper : servletWrappers) {
            Integer loadOnStartup = servletWrapper.getLoadOnStartup();
            if (loadOnStartup < 0) {
                continue;
            }
            ArrayList<ServletWrapper> list = map.computeIfAbsent(loadOnStartup, k -> new ArrayList<>());
            list.add(servletWrapper);
        }
        for (ArrayList<ServletWrapper> list : map.values()) {
            for (ServletWrapper servletWrapper : list) {
                servletWrapper.load();
            }
        }
    }

    public void destroy() throws ServletException {
        for (ServletWrapper wrapper : servletWrapperMap.values()) {
            wrapper.getServlet().destroy();
        }
        for (FilterWrapper wrapper : filterWrapperMap.values()) {
            wrapper.getFilter().destroy();
        }
        servletWrapperMap.clear();
        filterWrapperMap.clear();
        sessionMap.clear();
        attributes.clear();
    }


    // 读取配置文件，初始化servletMap 和 filterMap
    private void createWrapper() throws Exception {
        File webXml = new File(docBase, Constant.WEB_XML);
        if (!webXml.exists()) {
            throw new FileNotFoundException("找不到配置文件:" + webXml);
        }
        XmlUtil.parseServlet(this, webXml, servletWrapperMap);
        XmlUtil.parseFilter(this, webXml, filterWrapperMap);
    }

    public ServletWrapper findServletWrapper(String url) throws Exception {
        if (StrUtil.isEmpty(url)) {
            throw new Exception("url无效！");
        }

        for (ServletWrapper servletWrapper : servletWrapperMap.values()) {
            if (match(url, servletWrapper.getUrlPattern())) {
                return servletWrapper;
            }
        }

        return null;
    }

    public List<Filter> getMatchFilters(String urlPattern) throws BadRequestException {
        if (StrUtil.isEmpty(urlPattern)) {
            throw new BadRequestException("url无效");
        }
        List<Filter> list = new ArrayList<>();
        for (FilterWrapper wrapper : filterWrapperMap.values()) {
            if (match(urlPattern, wrapper.getUrlPattern())) {
                list.add(wrapper.getFilter());
            }
        }
        return list;
    }

    /**
     * 根据urlPattern判断是否匹配，参考https://juejin.cn/post/6844903604109885447
     * @param url
     * @param urlPattern
     * @return
     */
    private boolean match(String url, String urlPattern) {
        // 精确匹配
        if (StrUtil.equals(url, urlPattern)) return true;

        int index = urlPattern.indexOf("*");
        if (index != -1) {
            String sub = urlPattern.substring(0, index);
            // 路径匹配 比如/user/* 可以匹配 /user/
            if (!StrUtil.isEmpty(sub) && sub.equals(url)) return true;

            // 扩展名匹配
            // /user/user.jsp 可以匹配 *.jsp
            sub = urlPattern.substring(index + 1);
            if (!StrUtil.isEmpty(sub)) {
                String urlExt = StrUtil.subAfter(url, ".", false);
                if (sub.equals(urlExt)) return true;
            }
        }

        // 缺省匹配
        // "/" 匹配任意路径
        return StrUtil.equals(urlPattern, "/");

    }

    public void invalidateSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public HttpSession createSession(Response response) {
        HttpSession session = new HttpSession(UUID.randomUUID().toString().toUpperCase(Locale.ROOT), this);
        sessionMap.put(session.getId(), session);
        response.addCookie(new Cookie("JSESSIONID", session.getId()));
        return session;
    }


}
