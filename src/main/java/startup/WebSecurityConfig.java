package startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    private ResourceLoader resourceLoader;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**")
                .hasRole("USER")
                .and()
                .formLogin()
                .and()
                .csrf().disable();
        logger.info("Security is up!");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(createUser("username", "password", "USER"));
        userDetailsManager.createUser(createUser("demo", "demo", "USER"));
        userDetailsManager.createUser(createUser("mlkr", "password", "USER"));

        return userDetailsManager;
    }

    private UserDetails createUser(String username, String password,String role) {
        UserDetails details = User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles(role)
                .build();
        logger.info("Added demo user: {} ", details.toString());
        return details;
    }
}