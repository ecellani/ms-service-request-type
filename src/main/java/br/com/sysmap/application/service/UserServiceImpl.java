package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ecellani on 01/06/17.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final Map<Integer, User> users = new TreeMap<>();

    public UserServiceImpl() {
        users.put(1, new User(1, "Anderson dos Santos"));
        users.put(2, new User(2, "Érick Cellani"));
        users.put(3, new User(3, "Rodrigo Castilho"));
    }

    @Override
    public User findUser(Integer id) {
        return users.get(id);
    }

    @Override
    public Collection<User> findUsers() {
        return users.values();
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }
}
