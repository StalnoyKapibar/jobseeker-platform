package com.jm.jobseekerplatform.security;

import com.fasterxml.jackson.core.JsonParseException;
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

    private static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority("ROLE_ADMIN");
    private static final String SEEKER_PROFILE_ID = "seekerProfileId";
    private static final String JSON_HEADER_VALUE = "application/json";
    private static final List<String> OPEN_ACCESS_METHODS = Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.HEAD.name(),
            HttpMethod.OPTIONS.name());

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
            HttpServletRequest requestEff = request;
            logger.debug("Parameters: {{}}", Collections.list(request.getParameterNames())
                    .stream()
                    .map(p -> p + ": " + requestEff.getParameter(p))
                    .collect(Collectors.joining(", ")));
            if (request.getHeader(HttpHeaders.CONTENT_TYPE).contains(JSON_HEADER_VALUE) && request.getContentLength() > 0) {
                if (!(request instanceof ReadTwiceHttpServletRequestWrapper)) {
                    request = new ReadTwiceHttpServletRequestWrapper(request);
                }
                try {
                    JsonNode jsonNode = getJsonNode(request);
                    if (jsonNode != null && jsonNode.has(SEEKER_PROFILE_ID)) {
                        JsonNode seekerProfileIdNode = jsonNode.get(SEEKER_PROFILE_ID);
                        if (seekerProfileIdNode.isNumber()) {
                            bodySeekerProfileId = seekerProfileIdNode.asLong();
                            logger.debug("Request body {}: {} (logged user profile id: {})", SEEKER_PROFILE_ID,
                                    bodySeekerProfileId, loggedUser.getProfile().getId());
                        } else {
                            response.sendError(HttpStatus.BAD_REQUEST.value(), SEEKER_PROFILE_ID + "is not a number");
                            return;
                        }
                    }
                } catch (JsonParseException jpe) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), jpe.getMessage());
                    return;
                }
            }
            if (request.getParameterMap().size() > 0) {
                String seekerProfileIdStr = request.getParameter(SEEKER_PROFILE_ID);
                if (NumberUtils.isParsable(seekerProfileIdStr)) {
                    urlSeekerProfileId = Long.parseLong(seekerProfileIdStr);
                    logger.debug("Request urlencoded {}: {} (logged user profile id: {})", SEEKER_PROFILE_ID,
                            urlSeekerProfileId, loggedUser.getProfile().getId());
                } else {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), SEEKER_PROFILE_ID + "is not a number");
                    return;
                }
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
        if (user != null) {
            return user.getAuthority().equals(ADMIN_AUTHORITY);
        } else {
            return false;
        }
    }

    private boolean isOpenAccess(HttpServletRequest request) {
        return OPEN_ACCESS_METHODS.contains(request.getMethod());
    }

    private JsonNode getJsonNode(HttpServletRequest request) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            logger.debug("Json node: {}", jsonNode);
            return jsonNode;
        } catch (JsonParseException jpe) {
            logger.debug("Exception: {}", jpe.toString());
            throw jpe;
        }
    }

}
