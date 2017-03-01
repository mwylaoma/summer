/*
 * 广州理德物联网科技有限公司
 * Copyright (c) 2016.
 */

package tam.summer.example.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tanqimin on 2014/7/30.
 */
@Component
public class CorsFilter
        implements Filter {


    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control");
        chain.doFilter(req, res);
    }

    public void destroy() {

    }
}
