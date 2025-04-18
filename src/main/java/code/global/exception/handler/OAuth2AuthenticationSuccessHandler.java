package code.global.exception.handler;

import code.domain.oauth.entity.common.OAuth2UserDetailsImpl;
import code.domain.oauth.util.OAuth2AuthorizationRequestRepository;
import code.domain.redis.service.RedisService;
import code.domain.user.repository.UserRepository;
import code.global.security.domain.TokenResponse;
import code.global.security.jwt.util.CookieUtil;
import code.global.security.jwt.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static code.domain.oauth.util.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedisService redisService;

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException{

        OAuth2UserDetailsImpl oAuth2UserDetails = (OAuth2UserDetailsImpl) authentication.getPrincipal();

        String targetUrl = setTargetUrl(request, response, oAuth2UserDetails);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String setTargetUrl(HttpServletRequest request, HttpServletResponse response, OAuth2UserDetailsImpl oAuth2UserDetails){
        Optional<String> redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM)
                .map(Cookie::getValue);

        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());

        if(userRepository.existsByEmail(oAuth2UserDetails.getName())){
            TokenResponse tokenResponse = jwtProvider.createToken(oAuth2UserDetails.getName());

            response.addHeader(HttpHeaders.SET_COOKIE, CookieUtil.createCookie("access-token", tokenResponse.getAccessToken(), tokenResponse.getExpiredTime()).toString());

            log.info("[ setTargetUrl() ] : 서비스 회원입니다. 메인페이지로 리다이랙트합니다.");

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .path("/main")
                    .build()
                    .toUriString();
        }else {
            redisService.saveOAuth2UserInfo(oAuth2UserDetails.getUserInfo().getId(), oAuth2UserDetails.getName(), oAuth2UserDetails.getUserInfo().getProvider());

            log.info("[ setTargetUrl() ] : 서비스 회원이 아닙니다. 회원가입 페이지로 리다이랙트합니다.");

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .path("/sign-up/oauth")
                    .queryParam("provider-id", oAuth2UserDetails.getUserInfo().getId().toString())
                    .build()
                    .toUriString();
        }
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        oAuth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);
    }
}
