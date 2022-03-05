package servlet;

import exception.ServletException;
import http.Request;
import http.Response;
import util.Handler;

/**
 * @Author: pyhita
 * @Date: 2022/3/5
 * @Descrption: servlet
 * @Version: 1.0
 */
public class DefaultServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) throws ServletException {
        Handler.handleResource(request,response);
    }

    @Override
    public void doPost(Request request, Response response) throws ServletException {
        doGet(request,response);
    }
}
