//package com.viettel.automl.security.jwt;
//
//import com.viettel.automl.security.DomainUserDetailsService;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private final TokenProvider tokenProvider;
//    private final DomainUserDetailsService domainUserDetailsService;
//
//    public JWTConfigurer(TokenProvider tokenProvider, DomainUserDetailsService domainUserDetailsService) {
//        this.tokenProvider = tokenProvider;
//        this.domainUserDetailsService = domainUserDetailsService;
//    }
//
//    @Override
//    public void configure(HttpSecurity http) {
//        JWTFilter customFilter = new JWTFilter(tokenProvider, domainUserDetailsService);
//        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
