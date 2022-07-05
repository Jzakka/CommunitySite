package CommunitySIte.demo.web.argumentresolver;

import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("parameter 지원 여부 확인중...");

        boolean parameterHasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasUsersType = Users.class.isAssignableFrom(parameter.getParameterType());

        return parameterHasLoginAnnotation && hasUsersType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("인자를 resolve 하는 중...");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();
        if (session==null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_USER);
    }
}
