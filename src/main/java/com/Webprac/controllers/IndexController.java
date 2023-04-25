package com.Webprac.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class IndexController {
    @RequestMapping(value = {"/index"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/persons" )
    public String allPersons() {
        return "persons";
    }

//    @RequestMapping(value = "/events")
//    public String listEvents() {
//        return "events";
//    }
}
