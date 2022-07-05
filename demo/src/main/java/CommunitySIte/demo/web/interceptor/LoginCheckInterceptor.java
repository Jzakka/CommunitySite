package CommunitySIte.demo.web.interceptor;

import CommunitySIte.demo.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("로그인 세션 유효성 확인중 ...  {}", requestURI);
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(SessionConst.LOGIN_USER) == null) {
            log.info("인증되지 않은 사용자로부터 요청 수신");

            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
