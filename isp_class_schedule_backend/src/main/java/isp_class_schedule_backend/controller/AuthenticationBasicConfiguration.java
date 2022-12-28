package isp_class_schedule_backend.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class AuthenticationBasicConfiguration {

    /*protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/**").permitAll()
                .and()
                .csrf().disable()
                .httpBasic();
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                //.cors()
                //.and()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/api/**").permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.DELETE)
                .hasRole("ADMIN")
                .requestMatchers("/admin/**")
                .hasAnyRole("ADMIN")
                .requestMatchers("/user/**")
                .hasAnyRole("USER", "ADMIN")
                .requestMatchers("/login/**")
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }*/

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8001/"));
        configuration.setAllowedMethods(List.of("GET"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
