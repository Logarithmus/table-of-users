package socialcrud;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .antMatcher("/**")
          	.authorizeRequests()
        	.antMatchers("/users", "/facebook")
        		.authenticated()
      		.anyRequest()
        		.permitAll();
  	}
}