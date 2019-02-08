package socialcrud.controllers;

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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import socialcrud.User;
import socialcrud.UserRepository;

@Controller
public class UsersController {
    @Autowired 
    private UserRepository userRepository;

    @RequestMapping("/users")
    public String users(SecurityContextHolder context, HttpServletRequest request, Model model,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) ArrayList<String> checkbox) {

        var currentUser = (User)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        Optional<User> dbUser = userRepository.findById(currentUser.getId());
        boolean isBanned = dbUser.isPresent() && dbUser.get().getIsBanned();
        if (isBanned || dbUser.isEmpty()) {
            new SecurityContextLogoutHandler().logout(request, null, null);
            return "redirect:/banned";
        }

        if ((action != null) && (checkbox != null)) {
            switch (action) {
                case "delete": checkbox.forEach(id -> userRepository.deleteById(id)); break;
                case "ban":    checkbox.forEach(id -> userRepository.updateIsBanned(id, true)); break;
                case "unban":  checkbox.forEach(id -> userRepository.updateIsBanned(id, false)); break;
            }
        }
        
        List<User> usersList = userRepository.findAll();
        model.addAttribute("users", usersList);
        model.addAttribute("currentUserId", currentUser.getId());
        return "users";
    }
    
    @GetMapping("/banned")
    public String banned() {
        return "banned";
    }
}