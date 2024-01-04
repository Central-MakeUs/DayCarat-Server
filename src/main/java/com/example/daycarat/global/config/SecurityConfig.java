package com.example.daycarat.global.config;


import com.example.daycarat.global.jwt.JwtAuthFilter;
import com.example.daycarat.global.jwt.TokenService;
import com.example.daycarat.global.oauth.CustomOAuth2UserService;
import com.example.daycarat.global.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;
    private final String[] SwaggerPatterns = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    private final String[] BasicPatterns = {
            "/health"
    };

    private final String[] UserPatterns = {
            "/user/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(SwaggerPatterns).permitAll()
                        .requestMatchers(BasicPatterns).permitAll()
                        .requestMatchers(UserPatterns).authenticated()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                )
                .addFilterBefore(new JwtAuthFilter(tokenService),
                        UsernamePasswordAuthenticationFilter.class)
                .oauth2Login().loginPage("/token/expired")
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oAuth2UserService);

        http.addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
