package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.UserDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface UserService<T extends UserDTO> {
    T createUser(T user);
    T getUserById(int id);
    List<T> getAllUsers();
    T updateUser(int id, T user);
    void deleteUser(int id);
}
