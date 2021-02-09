package kz.edu.astanait.configs;

import kz.edu.astanait.model.User;
import kz.edu.astanait.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserRepository userRepo;

    @Bean
    public UserDetailsService userDetailsService(){
        return new MyUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/register").permitAll()
                .antMatchers("/accounts/**").hasAnyRole("user","admin")
                .antMatchers("/users/show_profile").hasAnyRole("user","admin")
                .antMatchers("/users/showHistory").hasAnyRole("user","admin")
                .antMatchers("/users/converted_version").hasAnyRole("user","admin")
                .antMatchers("/users/getAllPayments").hasAnyRole("admin")
                .antMatchers("/users/showAllUsers").hasAnyRole("admin")
                .antMatchers("/api/**").permitAll()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler())
                .usernameParameter("email")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return ((request, response, authentication) -> {
            String email = request.getParameter("email");
            User user = userRepo.findByEmail(email);

            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(60 * 5); //5 minutes
            session.setAttribute("user", user);

            response.sendRedirect("/");
        });
    }

}
