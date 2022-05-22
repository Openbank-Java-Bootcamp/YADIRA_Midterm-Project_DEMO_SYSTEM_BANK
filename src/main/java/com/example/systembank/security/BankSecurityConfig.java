package com.example.systembank.security;

import com.example.systembank.security.filters.CustomAuthenticationFilter;
import com.example.systembank.security.filters.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class BankSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/roles").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/account_holders").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/accounts/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/accounts").hasAnyAuthority("ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(PATCH, "/api/accounts/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/accounts/status/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/accounts/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/transfers/account-account").hasAnyAuthority("ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(POST, "/api/transfers/account-third_party").hasAnyAuthority("ACCOUNT_HOLDER");
        http.authorizeRequests().antMatchers(POST, "/api/transfers/third_party-account/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
