package com.crud;

import java.util.List;

public interface UserDao {
    public List<UserVO> queryAll();

    public List<UserVO> queryByName(String userName);

    public UserVO queryById(String userId);

    public void insert(UserVO user);

    public void update(UserVO user);

    public void delete(UserVO user);
}
