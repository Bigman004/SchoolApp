package com.example.SchoolApp.controller;

import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.service.LinkService;
import com.example.SchoolApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/template")
@Slf4j
public class TemplateController {

    private LinkService linkService;
    private UserService userService;

    @Autowired
    public TemplateController(LinkService linkService, UserService userService) {
        this.linkService = linkService;
        this.userService = userService;
    }

    @GetMapping("/send_link/{linkToken}")
    public String send_link(@PathVariable String linkToken, Model model) {
        model.addAttribute("linkToken", linkToken);
        log.info("send_link");
        if(linkService.linkExist(linkToken)) {
            log.info(linkService.linkExist(linkToken).toString() + ": link exist");
            return "login";
        }
        else {
            log.info(linkService.linkExist(linkToken).toString() + ": link exist");
            return "error";
        }
    }
    @PostMapping("/send_link/{linkToken}")
    public String changePassword(Model model,
            @ModelAttribute("password1") String password1,
                                 @ModelAttribute("password2") String password2, @PathVariable String linkToken) {
        if(password1.equals(password2) && linkService.linkExist(linkToken)) {
            log.info("changePassword");
            System.out.println(password1);
            System.out.println(password2);
            String username = linkService.findByLinkToken(linkToken);
            userService.changePassword(username, password1);
            return "success";
        }
        else{
            model.addAttribute("message", "link expired or does not exist" );
            log.info("link expired or does not exist");
        }
       return "error";
    }
}
