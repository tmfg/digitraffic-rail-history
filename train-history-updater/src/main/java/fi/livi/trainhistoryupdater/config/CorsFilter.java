package fi.livi.trainhistoryupdater.config;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CorsFilter  implements Filter {

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request = (HttpServletRequest) req;

        //spring-websocket manages these
        if (!request.getRequestURI().contains("/websockets/")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        chain.doFilter(req, res);
    }

    @Override
    public void init(final FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
