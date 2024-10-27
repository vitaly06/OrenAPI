package oksei.ru.OrenAPI.Controllers;

import oksei.ru.OrenAPI.DAO.UserDAO;
import oksei.ru.OrenAPI.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password) {
        User user = new User();
        try {
            user = userDAO.getUser(login, password);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (user.getLogin() != null) {
            return "true";
        }
        return "false";
    }
}
