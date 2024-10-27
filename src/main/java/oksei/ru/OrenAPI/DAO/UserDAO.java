package oksei.ru.OrenAPI.DAO;

import oksei.ru.OrenAPI.Mappers.UserMapper;
import oksei.ru.OrenAPI.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDAO() {
        jdbcTemplate = null;
    }

    public User getUser(String login, String password) {
        return jdbcTemplate.query("SELECT * FROM users WHERE login = ? AND password = ?",
                new Object[]{login, password}, new UserMapper())
                .stream().findAny().orElseThrow(null);
    }
}
