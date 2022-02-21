package catalina;

import cn.hutool.core.util.StrUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import http.HttpSession;
import lombok.Data;

import java.util.Map;

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

//    private WebappClassLoader classLoader;
    private Map<String, ServletWrapper> servletWrapperMap;
//    private Map<String, FilterWrapper> filterWrapperMap;

    private Map<String, HttpSession> sessionMap;
    private Map<String, Object> attributes;

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


}
