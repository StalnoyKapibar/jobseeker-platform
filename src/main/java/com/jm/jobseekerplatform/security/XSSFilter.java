/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class XSSFilter implements Filter {
    private static Logger logger = Logger.getLogger(String.valueOf(XSSFilter.class));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        System.out.println(httpServletRequest.getMethod());
        if("POST".equals(httpServletRequest.getMethod()) | "UPDATE".equals(httpServletRequest.getMethod())) {
            logger.info("start filter");
            ReadTwiceHttpServletRequestWrapper readTwiceHttpServletRequestWrapper = new ReadTwiceHttpServletRequestWrapper((HttpServletRequest) request);
            String newBody = cleanXSS(readTwiceHttpServletRequestWrapper.getBody());
            readTwiceHttpServletRequestWrapper.setBody(newBody);
            chain.doFilter(readTwiceHttpServletRequestWrapper, response);
        } else {
            chain.doFilter(request,response);
        }
    }

    private String cleanXSS(String value) {
        // You'll need to remove the spaces from the html entities below
        logger.info("In cleanXSS RequestWrapper ..............." + value);
  //      value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
    //    value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
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
        logger.info("Out cleanXSS RequestWrapper ........ value ......." + value);
        return value;
    }
}
