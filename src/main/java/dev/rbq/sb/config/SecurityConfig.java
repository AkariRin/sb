package dev.rbq.sb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（使用Session管理，API场景）
                .csrf(AbstractHttpConfigurer::disable)

                // 配置授权规则：默认所有路由公开
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 禁用默认表单登录
                .formLogin(AbstractHttpConfigurer::disable)

                // 禁用默认HTTP Basic认证
                .httpBasic(AbstractHttpConfigurer::disable)

                // 禁用默认登出页面（使用自定义登出接口）
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * 密码编码器（BCrypt）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

