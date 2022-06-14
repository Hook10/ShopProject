package com.shop.admin.user;

import com.shop.common.entity.Role;
import com.shop.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userNamHM = new User("nam@email.com", "nam2020", "Nam", "Ham");
        userNamHM.addRole(roleAdmin);

        User savedUser = repo.save(userNamHM);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userRavi = new User("ravi@gmail.com", "ravi2020", "Ravi", "Kumar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);

        User savedUser = repo.save(userRavi);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        List<User> listUsers = (List<User>) repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isGreaterThan(0);
    }

    @Test
    public void testGetUserById() {
        User user = repo.findById(1).get();
        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User user = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("updated@gmail.com");

        repo.save(user);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(2);
        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesPerson);

        repo.save(user);

    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repo.deleteById(userId);

        Optional<User> byId = repo.findById(userId);

        assertThat(byId).isEmpty();
    }


}
