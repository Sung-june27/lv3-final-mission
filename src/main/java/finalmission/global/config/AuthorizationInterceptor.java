package finalmission.global.config;


import finalmission.global.error.exception.ForbiddenException;
import finalmission.global.error.exception.UnauthorizedException;
import finalmission.global.util.JwtExtractor;
import finalmission.member.domain.LoginMember;
import finalmission.member.domain.Role;
import finalmission.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        CheckRole checkRole = handlerMethod.getMethodAnnotation(CheckRole.class);
        if (checkRole == null) {
            return true;
        }

        String token = JwtExtractor.extractFromRequest(request);
        LoginMember loginMember = authService.getLoginMemberByToken(token);
        if (loginMember == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }
        Role[] roles = checkRole.value();
        if (Arrays.stream(roles).noneMatch(role -> role == loginMember.role())) {
            throw new ForbiddenException("접근 불가한 페이지입니다.");
        }
        return true;
    }
}
