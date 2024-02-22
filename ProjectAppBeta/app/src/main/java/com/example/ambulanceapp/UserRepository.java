package com.example.ambulanceapp;

import java.util.List;

// UserRepository.java
// UserRepository.java
public class UserRepository {
    private UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public Users login(String name, String password) {
        return userDao.login(name, password);
    }

    public void deleteUser(Users user) {
        userDao.delete(user);
    }
    public void register(Users user) {
        userDao.insert(user);
    }
    public List<Users> getAllUsers() {
        return userDao.getAllUsers();
    }
}


