package com.cs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        
        http.httpBasic().and().authorizeRequests()
        .antMatchers("/h2-console/**").hasRole("ADMIN") // allowed access to ADMIN user only
        .antMatchers("/api/v1/**").hasRole("ADMIN")
        .anyRequest().authenticated()//all other urls can be access by any authenticated role
        .and().formLogin()//enable form login instead of basic login
        .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
        .and().csrf().ignoringAntMatchers("/api/v1/**")
        .and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls
        
        }
  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth.inMemoryAuthentication()
        .withUser("admin").password(passwordEncoder().encode("admin"))
        .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}