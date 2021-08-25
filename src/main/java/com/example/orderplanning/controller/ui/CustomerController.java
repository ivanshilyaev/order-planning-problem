package com.example.orderplanning.controller.ui;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/addCustomer")
    public String addCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/addCustomer";
    }

    @PostMapping("/addCustomer")
    public String addCustomerSubmit(@ModelAttribute Customer customer) {
        customerService.saveOrUpdate(customer);
        return "redirect:/index";
    }

    @GetMapping("/allCustomers")
    public String listAllCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customer/allCustomers";
    }

    @GetMapping("/deleteCustomerById")
    public String deleteCustomerByIdForm() {
        return "customer/deleteCustomerById";
    }

    @PostMapping("/deleteCustomerById")
    public String deleteCustomerByIdSubmit(@RequestParam(value = "id") String id) {
        customerService.deleteById(id);
        return "redirect:/index";
    }
}
