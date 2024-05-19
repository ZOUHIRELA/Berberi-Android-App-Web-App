package com.berberi.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController{

    @RequestMapping("/")
    public String profile(){
        return "Hello World in profile page";
    }
}
