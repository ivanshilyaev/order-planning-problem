package com.example.orderplanning.controller;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import com.example.orderplanning.service.OrderService;
import com.example.orderplanning.service.ProductService;
import com.example.orderplanning.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderPlanningController {

    private final WarehouseService warehouseService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderPlanningController(WarehouseService warehouseService,
                                   CustomerService customerService,
                                   OrderService orderService,
                                   ProductService productService
    ) {
        this.warehouseService = warehouseService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    // customers' API

    @GetMapping("/addCustomer")
    public String addCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "addCustomer";
    }

    @PostMapping("/addCustomer")
    public String addCustomerSubmit(@ModelAttribute Customer customer) {
        customerService.saveOrUpdate(customer);
        return "redirect:/index";
    }

    @GetMapping("/allCustomers")
    public String listAllCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "allCustomers";
    }

    // orders' API

    @GetMapping("/createOrder")
    public String createOrder() {
        return "createOrder";
    }
}
