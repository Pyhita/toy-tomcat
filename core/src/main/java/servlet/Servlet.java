package servlet;

import exception.ServletException;
import http.Request;
import http.Response;

/**
 * @Autuor: pyhita
 * @Date: 2022/1/19 - 01 - 19 - 21:10
 * @Description: servlet接口
 * @Version: 1.0
 */
public interface Servlet {

    void init() throws ServletException;
    void destroy() throws ServletException;
    void service(Request request, Response response) throws ServletException;

}

