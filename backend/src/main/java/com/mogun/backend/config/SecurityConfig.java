package com.mogun.backend.config;


import com.mogun.backend.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // Spring Security Filter 가 Spring Filter Chain 에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secure annotation 활성화, preAuthorize annotation 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                // 루트 아래 user or manager or admin 주소가 포함된 곳으로 이동할 경우 아래와 같은 조건이 필요
                .antMatchers("/user/**").authenticated() // user 아래 모든 경로는 인증 후 접근 가능
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // 이외의 경우는 모두 일반 접근을 허용
                .anyRequest().permitAll()
                // 그리고 Login page 는 /login 주소로 향할 것
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                // '/login' 주소가 호출이 되면 Security 가 낚아 채서 대신 로그인 진행
                .defaultSuccessUrl("/")
                // oauth 로그인 처리
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                // 로그인이 완료된 뒤의 후처리 필요 ->
                // 1. 코드 받기(인증), 2. 접근 토큰(권한), 3. 사용자 프로필 정보 로드, 4. 프로필 정보를 토대로 회원가입 등을 유도
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
