package com.jm.jobseekerplatform.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.jobseekerplatform.model.users.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SeekerApiAccessFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SeekerApiAccessFilter.class);

    private static final List<String> OPEN_ACCESS_METHODS = Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.HEAD.name(),
            HttpMethod.OPTIONS.name());
    private static final String JSON_HEADER_VALUE = "application/json";
    private static final String URLENCODED_HEADER_VALUE = "application/x-www-form-urlencoded";
    private static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority("ROLE_ADMIN");
    private static final String SEEKER_PROFILE_ID = "seekerProfileId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.debug("Request method: {}", request.getMethod());
        logger.debug("Request URI: {}", request.getRequestURI());
        User loggedUser = getLoggedUser();
        if (!(isAdmin(loggedUser) || isOpenAccess(request))) {
            Long urlSeekerProfileId = null;
            Long bodySeekerProfileId = null;
            logger.debug("Parameters: {}", Collections.list(request.getParameterNames()));
            HttpServletRequest requestEff = request;
//            logger.debug("Headers: {}", Collections.list(request.getHeaderNames()).stream()
//                    .map(x -> x + ": " + requestEff.getHeader(x))
//                    .collect(Collectors.joining(", ")));
            if (request.getHeader(HttpHeaders.CONTENT_TYPE).contains(JSON_HEADER_VALUE) &&
                    Long.parseLong(request.getHeader(HttpHeaders.CONTENT_LENGTH)) > 0) {
                if (!(request instanceof ReadTwiceHttpServletRequestWrapper)) {
                    request = new ReadTwiceHttpServletRequestWrapper(request);
                }
                JsonNode jsonNode = getJsonNode(request);
                bodySeekerProfileId = jsonNode != null ? jsonNode.get(SEEKER_PROFILE_ID).asLong() : null;
                logger.debug("Body seeker profile: logged id: {}  request id: {}", loggedUser.getProfile().getId(), bodySeekerProfileId);
            }
            if (request.getParameterMap().size() > 0) {
                String seekerProfileIdStr = request.getParameter(SEEKER_PROFILE_ID);
                urlSeekerProfileId = NumberUtils.isParsable(seekerProfileIdStr) ? Long.parseLong(seekerProfileIdStr) : null;
                logger.debug("Urlencoded seeker profile: logged id: {}  request id: {}", loggedUser.getProfile().getId(), urlSeekerProfileId);
            }
            if (urlSeekerProfileId != null && !loggedUser.getProfile().getId().equals(urlSeekerProfileId) ||
                    bodySeekerProfileId != null && !loggedUser.getProfile().getId().equals(bodySeekerProfileId)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isAdmin(User user) {
        return user.getAuthority().equals(ADMIN_AUTHORITY);
    }

    private boolean isOpenAccess(HttpServletRequest request) {
        return OPEN_ACCESS_METHODS.contains(request.getMethod());
    }

    private JsonNode getJsonNode(HttpServletRequest request) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        logger.debug("Json node: {}", jsonNode);
        return jsonNode;
    }

}
