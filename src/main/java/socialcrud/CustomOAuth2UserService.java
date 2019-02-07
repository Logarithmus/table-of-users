package socialcrud;

import java.util.Map;
import java.util.Date;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import socialcrud.UserRepository;
import socialcrud.User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map attributes = oauth2User.getAttributes();
        User user = new User();
        user.setId((String) attributes.get("sub"));
        user.setProvider("Google");
        user.setName((String) attributes.get("name"));
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        user.setLastLogin(timestamp);
        userRepository.save(user);
        return oauth2User;
    }
}