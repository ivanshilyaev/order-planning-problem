package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.CustomerModelAssembler;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    Customer customer1 = new Customer(1L, "User1", 50, 50);
    Customer invalidCustomer1 = new Customer(1L, "", 50, 50);
    Customer customer2 = new Customer(2L, "User2", 30, 60);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerService service;
    @MockBean
    private CustomerModelAssembler assembler;

    @BeforeEach
    public void setUp() {
        Mockito.when(assembler.toModel(customer1)).thenReturn(EntityModel.of(customer1,
                linkTo(methodOn(CustomerController.class).one(customer1.getId())).withSelfRel()));
        Mockito.when(assembler.toModel(customer2)).thenReturn(EntityModel.of(customer2,
                linkTo(methodOn(CustomerController.class).one(customer2.getId())).withSelfRel()));
    }

    @Test
    public void allCustomersSuccess() throws Exception {
        Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(customer1, customer2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customers")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customerList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.customerList[1].name", is(customer2.getName())));
    }

    @Test
    public void newCustomerSuccess() throws Exception {
        Mockito.doNothing().when(service).save(customer1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(customer1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(customer1.getName())));
    }

    @Test
    public void newCustomerFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("Name is mandatory")));
    }

    @Test
    public void oneCustomerSuccess() throws Exception {
        Mockito.when(service.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customers/" + customer1.getId())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", is(customer1.getX())));
    }

    @Test
    public void updateCustomerSuccess() throws Exception {
        Mockito.doNothing().when(service).save(customer1);
        Mockito.when(service.update(customer1, customer1.getId())).thenReturn(customer1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/customers/" + customer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(customer1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(customer1.getName())));
    }

    @Test
    public void deleteCustomerSuccess() throws Exception {
        Mockito.when(service.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/customers/" + customer1.getId())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());
    }
}
