package CommunitySIte.demo.web;

import CommunitySIte.demo.web.argumentresolver.LoginUserArgumentResolver;
import CommunitySIte.demo.web.interceptor.LogInterceptor;
import CommunitySIte.demo.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico","/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/forum/{forumId}","/forum/{forumId}/category/**",
                        "/forum/{forumId}/post/**","/users/new","/login",
                        "/logout","/css/**", "/*.ico","/error");
    }
}
