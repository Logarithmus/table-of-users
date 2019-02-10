package socialcrud.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import socialcrud.User;
import socialcrud.UserRepository;

@Controller
public class UsersController {
    @Autowired 
    private UserRepository userRepository;

    private String handleUserBanOrDelete(User user, HttpServletRequest request) {
        Optional<User> dbUser = userRepository.findById(user.getId());
        boolean isBanned = dbUser.isPresent() && dbUser.get().getIsBanned();
        if (isBanned) {
            new SecurityContextLogoutHandler().logout(request, null, null);
            return "forward:/banned";
        }
        boolean isDeleted = dbUser.isEmpty();
        if (isDeleted) {
            new SecurityContextLogoutHandler().logout(request, null, null);
            return "forward:/deleted";
        }
        return null;
    }

    @RequestMapping("/users")
    public String users(SecurityContextHolder context, HttpServletRequest request, Model model,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) ArrayList<String> checkbox) {

        Logger logger = LoggerFactory.getLogger(UsersController.class);

        User currentUser = (User)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        String viewName = handleUserBanOrDelete(currentUser, request);
        if (viewName != null) {
            return viewName;
        }

        if ((action != null) && (checkbox != null)) {
            switch (action) {
                case "delete":
                    checkbox.forEach(id -> userRepository.deleteById(id)); 
                    if (checkbox.contains(currentUser.getId())) {
                        new SecurityContextLogoutHandler().logout(request, null, null);
                        return "forward:/deleted";
                    }
                    break;
                case "ban":
                    checkbox.forEach(id -> userRepository.updateIsBanned(id, true));
                    if (checkbox.contains(currentUser.getId())) {
                        new SecurityContextLogoutHandler().logout(request, null, null);
                        return "forward:/banned";
                    }
                    break;
                case "unban":
                    checkbox.forEach(id -> userRepository.updateIsBanned(id, false));
                    break;
            }
        }
        
        List<User> usersList = userRepository.findAll();
        model.addAttribute("users", usersList);
        model.addAttribute("currentUserId", currentUser.getId());
        return "users";
    }
    
    @RequestMapping("/banned")
    public String banned() {
        return "banned";
    }

    @RequestMapping("/deleted")
    public String deleted() {
        return "deleted";
    }
}