package com.example.orderplanning.controller.api;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerRestController.class)
public class CustomerRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    Customer customer1 = new Customer("Ivan", 50, 50);
    Customer invalidCustomer1 = new Customer("", 50, 50);
    Customer customer2 = new Customer("Pavel", 30, 60);

    @Test
    public void addCustomerSuccess() throws Exception {
        Mockito.doNothing().when(customerService).saveOrUpdate(customer1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/addCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("Ivan")));
    }

    @Test
    public void addCustomerFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/addCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id", is("Id is mandatory")));
    }

    @Test
    public void allCustomersSuccess() throws Exception {
        Mockito.when(customerService.findAll()).thenReturn(List.of(customer1, customer2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/allCustomers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is("Pavel")));
    }

    @Test
    public void deleteCustomerByIdSuccess() throws Exception {
        Mockito.when(customerService.findById(customer1.getId())).thenReturn(customer1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/deleteCustomerById?id=Ivan"))
                .andExpect(status().isOk());
    }
}
