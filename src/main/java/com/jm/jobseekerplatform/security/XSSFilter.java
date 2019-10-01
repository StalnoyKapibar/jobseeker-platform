/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class XSSFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XSSFilter.class);

    private static final List<String> REQUEST_BODY_METHODS = Arrays.asList(
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (REQUEST_BODY_METHODS.contains(((HttpServletRequest) request).getMethod())) {
            ReadTwiceHttpServletRequestWrapper requestWrapper;
            if (request instanceof ReadTwiceHttpServletRequestWrapper) {
                requestWrapper = (ReadTwiceHttpServletRequestWrapper) request;
            } else {
                requestWrapper = new ReadTwiceHttpServletRequestWrapper((HttpServletRequest) request);
                request = requestWrapper;
            }
            requestWrapper.setBody(cleanXSS(requestWrapper.getBody()));
        }
        chain.doFilter(request, response);
    }

    private String cleanXSS(String value) {
        logger.debug("In cleanXSS RequestWrapper ...............{}", value);
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

        value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
        value = value.replaceAll("(?i)&lt;script.*?&gt;.*?&lt;/script.*?&gt;", "");
        value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
        value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
        value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
        value = value.replaceAll("(?i)<script>", "");
        value = value.replaceAll("(?i)&lt;script&gt;", "");
        value = value.replaceAll("(?i)</script>", "");
        value = value.replaceAll("&lt;/script&gt;", "");
        logger.debug("Out cleanXSS RequestWrapper ..............{}", value);
        return value;
    }

}
