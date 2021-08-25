package com.example.orderplanning.controller.ui;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderPlanningErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
