package com.example.ciklo.config;

import com.example.ciklo.service.customer.CustomerDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {
        https
                .csrf()
                .disable()
                .cors()
                .and()

                //Authorize unrestricted when go to log in(authenticate) or register
                .authorizeHttpRequests()
                .requestMatchers("/ciklo/auth/**", "/ciklo/homepage/**", "/ciklo/bill/**", "/ws/**").permitAll()

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/custom/**", "/custom/css/page-error/**", "/js/**", "/css/**","/fonts/**", "/images/**", "/scss/**").permitAll()

                .and()
                //Restricted and only user role can pass
                .authorizeHttpRequests()
                .requestMatchers("/ciklo/cus/**").hasAnyAuthority("CUSTOMER")

                .and()
                //Restricted and only manager role can pass
                .authorizeHttpRequests()
                .requestMatchers("/ciklo/driver/**", "/ciklo/cyclo/status_cyclo", "/ciklo/cyclo/cyclo_driver").hasAnyAuthority("DRIVER")
//
                .and()
                //Restricted and only admin role can pass
                .authorizeHttpRequests()
                .requestMatchers("/ciklo/admin/**", "/ciklo/cyclo/**").hasAnyAuthority("ADMIN")

                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/ciklo/auth/authenticateForm")
                .usernameParameter("username").passwordParameter("password")

                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("uid", "access", "refresh")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/ciklo/auth/authenticateForm")

//                .and()
//                .rememberMe().userDetailsService(CustomerDetailServiceImpl)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return https.build();
    }
}
