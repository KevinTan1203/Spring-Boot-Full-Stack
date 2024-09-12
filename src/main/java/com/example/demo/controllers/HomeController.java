package com.example.demo.controllers;

import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String HelloWorld() {
        return "<h1>Hello World!</h1>";
    }

    @GetMapping("/about-us")
    // If the route is not marked with response body, then by default we are using templates
    /*
     * The Model model param is automatically passed to aboutUd when it is called by Spring.
     * This is known as the view model
    */
    public String AboutUs(Model model) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        model.addAttribute("currentDateTime", currentDateTime);
        
        // We need to return the file path to the template, relative to the resources/template
        return "about-us";
    }
}