package com.example.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContorllerT {



    @RequestMapping(value = "/hello")
    @CrossOrigin
    public String helloWorld(String name){
        return "hello world " + name;
    }


    @RequestMapping(value = "/getStudent")
    @CrossOrigin
    public Student getStudent(String name, String email){
        return new Student(name, email);
    }
}
