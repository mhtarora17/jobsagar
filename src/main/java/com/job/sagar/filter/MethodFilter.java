package com.job.sagar.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.job.sagar.constant.Constants.FAILED;

@Component
@Order(2)
@Log4j2
public class MethodFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    private Set<String> methods = Stream.of("GET", "POST")
            .collect(Collectors.toCollection(HashSet::new));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("inside MethodFilter to validate allowed method order 2: {}", request.getMethod());
        if (methods.contains(request.getMethod())) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            String message = HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase();
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(FAILED);
            errorResponse.setMessage(message);
            response.getWriter().write(objectMapper.writeValueAsString(
                    new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED)));
        }
    }
}