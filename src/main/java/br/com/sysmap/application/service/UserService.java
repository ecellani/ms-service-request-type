package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.User;

import java.util.Collection;

/**
 * Created by ecellani on 01/06/17.
 */
public interface UserService {

    User findUser(Integer id);

    Collection<User> findUsers();

    void updateUser(User user);
}
