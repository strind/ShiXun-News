package com.strind.user;

import com.strind.UserApplication;
import com.strind.model.common.RespResult;
import com.strind.model.user.dtos.LoginDto;
import com.strind.service.AppUserService;
import com.strind.service.impl.AppUserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/21 13:28
 */
@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class userTest {

    @Autowired
    private AppUserService appUserService;

    /**
     * description: 测试登录接口
     */
    @Test
    public void testLogin() {
        LoginDto dto = new LoginDto();
        dto.setPhone("13511223453");
        dto.setPassword("abc");
        RespResult login = appUserService.login(dto);
        System.out.println(login.getCode() + " " + login.getErrorMessage());
    }

}
