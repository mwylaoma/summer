package tam.summer.restdoc;

import tam.summer.common.ValidateUtil;
import tam.summer.restdoc.common.ResourceUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by tanqimin on 2015/11/14.
 */
public abstract class DocumentResourceServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();
        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());
        response.setCharacterEncoding("utf-8");
        String fullUrl;
        if (!"".equals(path)) {
            if ("/".equals(path)) {
                response.sendRedirect("index.html");
            } else if (path.contains(".json")) {
                fullUrl = path;
                if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                    fullUrl = path + "?" + request.getQueryString();
                }
                response.setContentType("application/json");
                response.getWriter().print(this.process(fullUrl));
            } else {
                ResourceUtil.returnResourceFile(path, uri, response);
            }
        } else {
            if (!contextPath.equals("") && !contextPath.equals("/")) {
                response.sendRedirect("doc/index.html");
            } else {
                response.sendRedirect("/doc/index.html");
            }
        }
    }

    protected abstract String process(String url) throws IOException;
}
