package filter;

import http.Request;
import http.Response;

/**
 * @Author: pyhita
 * @Date: 2022/2/24
 * @Descrption: filter
 * @Version: 1.0
 */
public interface FilterChain {
    void doFilter(Request request, Response response) ;
}