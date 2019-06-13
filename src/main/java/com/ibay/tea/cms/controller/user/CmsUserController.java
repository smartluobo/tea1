package com.ibay.tea.cms.controller.user;

import com.ibay.tea.entity.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("cms/user")
public class CmsUserController {


    public User findUserById(){
        return null;
    }
}
