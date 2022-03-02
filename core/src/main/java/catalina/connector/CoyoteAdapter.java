package catalina.connector;

import catalina.Context;
import catalina.Host;
import catalina.ServletWrapper;
import filter.Filter;
import filter.StandardFilterChain;
import http.Request;
import http.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * @Autuor: pyhita
 * @Date: 2021/12/22 - 12 - 22 - 22:16
 * @Description: catalina.connector
 * @Version: 1.0
 */
@Data
@Slf4j
@AllArgsConstructor
public class CoyoteAdapter {
    private Connector connector;

    public void doDispatch(Request request, Response response) throws Exception {
        for (Host host : connector.getEngine().getHosts()) {
            // 主机名字对不上，继续遍历
            // 1 根据host 确定主机
            if (!host.getName().equals(request.getHost())) continue;
            // 2 根据请求的URL确定context
            String url = request.getUrl();
            for (Map.Entry<String, Context> entry : host.getContextMap().entrySet()) {
                String path = entry.getKey();
                Context context = entry.getValue();
                if (url.startsWith(path)) {
                    request.setContext(context);
                    response.setContext(context);
                    break;
                }
            }
            if (request.getContext() != null) {
                break;
            }
        }
        // 调用service方法找到servlet
        service(request, response);
    }


    private void service(Request request, Response response) throws Exception {
        Context context = request.getContext();
        if (context != null) {
            String urlPattern = request.getUrl();
            // 消除掉uri中的path，得到servlet映射路径
            if (!"/".equals(context.getPath())) {
                urlPattern = urlPattern.replaceFirst(context.getPath(), "");
            }

            // 调用filterchain
            List<Filter> filters = context.getMatchFilters(urlPattern);
            if (!filters.isEmpty()) {
                StandardFilterChain filterChain = new StandardFilterChain(filters);
                filterChain.doFilter(request, response);
            }

            // 找到相应的servlet
            ServletWrapper servletWrapper = context.findServletWrapper(urlPattern);
            if (servletWrapper != null) {
                response.setStatus(HttpStatus.OK);
                servletWrapper.getServlet().service(request, response);
            } else {

            }
        }

    }


}
