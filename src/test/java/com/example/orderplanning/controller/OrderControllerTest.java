package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.OrderModelAssembler;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.service.OrderPlanningService;
import com.example.orderplanning.service.OrderService;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    Order order = new Order(1L, 1L, "iPhone", null, 0);
    Order invalidOrder = new Order(1L, 1L, "", null, 0);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService service;
    @MockBean
    private OrderPlanningService orderPlanningService;
    @MockBean
    private OrderModelAssembler assembler;

    @BeforeEach
    public void setUp() {
        Mockito.when(assembler.toModel(order)).thenReturn(EntityModel.of(order,
                linkTo(methodOn(CustomerController.class).one(order.getId())).withSelfRel()));
    }

    @Test
    public void allOrdersSuccess() throws Exception {
        Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.orderList[0].productName", is(order.getProductName())));
    }

    @Test
    public void newOrderSuccess() throws Exception {
        Mockito.doNothing().when(service).save(order);
        Mockito.doNothing().when(orderPlanningService).findNearestWarehouse(order);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(order));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.productName", is(order.getProductName())));
    }

    @Test
    public void newOrderFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(invalidOrder));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.productName", is("Product name is mandatory")));
    }

    @Test
    public void oneOrderSuccess() throws Exception {
        Mockito.when(service.findById(order.getId())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/" + order.getId())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is(order.getProductName())));
    }

    @Test
    public void searchOrdersSuccess() throws Exception {
        Mockito.when(service.findByCustomerIdAndProductName(order.getCustomerId(), order.getProductName()))
                .thenReturn(List.of(order));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders?customerId=" + order.getCustomerId()
                                + "&productName=" + order.getProductName())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.orderList[0].productName", is(order.getProductName())));
    }
}
