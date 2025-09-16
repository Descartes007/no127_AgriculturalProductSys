package com.agricultural.em.controller;

import com.agricultural.em.common.Result;
import com.agricultural.em.entity.User;
import com.agricultural.em.utils.TokenUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @PostMapping("/role")
    public Result getUserRole(){
        User currentUser = TokenUtils.getCurrentUser();
        return Result.success(currentUser.getRole());
    }
}
