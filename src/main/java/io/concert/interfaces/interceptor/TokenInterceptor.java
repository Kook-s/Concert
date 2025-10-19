package io.concert.interfaces.interceptor;

import io.concert.domain.service.QueueService;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final QueueService queueService;
    private static final String TOKEN = "Token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(TOKEN);

        log.info("Receive request for URI: {} with Token: {}", request.getRequestURI(), token);

        if (token == null || token.isEmpty()) {
            throw new CoreException(ErrorType.MISSING_TOKEN, null);
        }
        queueService.validateToken(token);
        return true;
    }
}
