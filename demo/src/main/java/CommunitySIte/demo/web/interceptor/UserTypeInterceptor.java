package CommunitySIte.demo.web.interceptor;

import CommunitySIte.demo.domain.UserType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.exception.NotAuthorizedException;
import CommunitySIte.demo.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class UserTypeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("사용자 권한 확인 중 ... {} ", requestURI);
        HttpSession session = request.getSession();

        Users loginUser = (Users)session.getAttribute(SessionConst.LOGIN_USER);

        if (session == null || loginUser==null
            || loginUser.getUserType() != UserType.ADMIN) {
            log.info("사용자 권한 없음");

            //홈화면이 아니라 오류화면으로 넘어가게 수정 필요
            //아님 알람창으로 경고를 띄운다던가
//            response.sendRedirect("/");
//            return false;
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }
        return true;
    }
}
