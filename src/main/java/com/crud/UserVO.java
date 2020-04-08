package com.crud;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Data
public class UserVO {
    private String userName;
    private String password;
    private String userId;

    //测试数据
    @Override
    public String toString() {
        return "userId=" + userId + ", passowrd=" + password + ", userName=" + userName;
    }
}
