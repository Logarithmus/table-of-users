package socialcrud;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.sql.Timestamp;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import socialcrud.extractors.*;

@EnableWebSecurity
@EnableOAuth2Client
@Order(101)
public class OAuth2Configuration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;
    @Autowired
    private FacebookPrincipalExtractor facebookPrincipalExtractor;
    @Autowired
    private GooglePrincipalExtractor googlePrincipalExtractor;
    @Autowired
    private GithubPrincipalExtractor githubPrincipalExtractor;

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter>
           oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        var reg = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        reg.setFilter(filter);
        reg.setOrder(-100);
        return reg;
    }

    @Bean
    @ConfigurationProperties("security.oauth2.facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.vk")
    public ClientResources vk() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.google")
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.github")
    public ClientResources github() {
        return new ClientResources();
    }

    private Filter ssoFilter(ClientResources client, String path,
                             PrincipalExtractor extractor) {
        var filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        var restTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(restTemplate);
        var tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
                                                      client.getClient().getClientId());
        tokenServices.setRestTemplate(restTemplate);
        tokenServices.setPrincipalExtractor(extractor);
        filter.setTokenServices(tokenServices);
        return filter;
    }

    public Filter ssoCompositeFilter() {
        var filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook", facebookPrincipalExtractor));
        filters.add(ssoFilter(vk(), "/login/vk", facebookPrincipalExtractor));
        filters.add(ssoFilter(google(), "/login/google", googlePrincipalExtractor));
        filters.add(ssoFilter(github(), "/login/github", githubPrincipalExtractor));
        filter.setFilters(filters);
        return filter;
    }
}

class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}
