package com.example.orderplanning.controller;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.Product;
import com.example.orderplanning.entity.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {
    private final long CUSTOMER_ID = 1L;
    private final long WAREHOUSE_1_ID = 1L;
    private final long WAREHOUSE_2_ID = 2L;
    private final long WAREHOUSE_3_ID = 3L;
    private final long PRODUCT_1_ID = 1L;
    private final long PRODUCT_2_ID = 2L;
    private final long PRODUCT_3_ID = 3L;
    private final String PRODUCT_NAME = "iPhone";
    private final double MIN_DISTANCE = 5;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        Warehouse warehouse1 = Warehouse.builder().id(WAREHOUSE_1_ID).name("Minsk").x(50).y(50).build();
        Warehouse warehouse2 = Warehouse.builder().id(WAREHOUSE_2_ID).name("Kiev").x(20).y(20).build();
        Warehouse warehouse3 = Warehouse.builder().id(WAREHOUSE_3_ID).name("Saint Petersburg").x(40).y(40).build();
        Product product1 = Product.builder().id(PRODUCT_1_ID).name(PRODUCT_NAME).warehouseId(WAREHOUSE_1_ID).build();
        Product product2 = Product.builder().id(PRODUCT_2_ID).name(PRODUCT_NAME).warehouseId(WAREHOUSE_2_ID).build();
        Product product3 = Product.builder().id(PRODUCT_3_ID).name(PRODUCT_NAME).warehouseId(WAREHOUSE_3_ID).build();
        Customer customer = Customer.builder().id(CUSTOMER_ID).name("User").x(45).y(50).build();

        mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(warehouse1)));
        mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(warehouse2)));
        mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(warehouse3)));

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(product1)));
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(product2)));
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(product3)));

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(customer)));
    }

    @Test
    public void newOrderSuccess() throws Exception {
        Order order = Order.builder().customerId(CUSTOMER_ID).productName(PRODUCT_NAME).build();

        MockHttpServletRequestBuilder mockRequest = post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(order));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.customerId", is((int) CUSTOMER_ID)))
                .andExpect(jsonPath("$.productName", is(PRODUCT_NAME)))
                .andExpect(jsonPath("$.warehouse.id", is((int) WAREHOUSE_1_ID)))
                .andExpect(jsonPath("$.distance", is(MIN_DISTANCE)));
    }
}
