package socialcrud.controllers;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@RestController
public class PrincipalController {
    @GetMapping("/auth")
    public Object auth(SecurityContextHolder contextHolder) {
        return contextHolder.getContext().getAuthentication().getPrincipal();
    }
}