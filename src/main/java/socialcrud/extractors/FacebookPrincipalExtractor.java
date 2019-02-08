package socialcrud.extractors;

import java.util.Map;
import java.util.Date;
import java.util.Optional;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import socialcrud.User;
import socialcrud.UserRepository;

@Component
public class FacebookPrincipalExtractor implements PrincipalExtractor {

	@Autowired
	private UserRepository userRepository;

	private Timestamp getTimestampNow() {
		Date date = new Date();
	   	return new Timestamp(date.getTime());
	}

	@Override
	public Object extractPrincipal(Map<String, Object> map) {
    	var id = (String) map.get("id");
    	var newUser = new User();
    	newUser.setId(id);
	    newUser.setProvider("Facebook");
	    newUser.setName((String) map.get("name"));
	    newUser.setLastLogin(getTimestampNow());
	    Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> {
        	newUser.setIsBanned(user.getIsBanned());
        	if (!newUser.getIsBanned()) {
	            userRepository.save(newUser);
        	}
        });
        if (optionalUser.isEmpty()) {
        	newUser.setIsBanned(false);
        	userRepository.save(newUser);
        }
        return newUser;
	}
}