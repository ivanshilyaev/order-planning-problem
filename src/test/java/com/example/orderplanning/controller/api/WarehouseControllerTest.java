package com.example.orderplanning.controller.api;

import com.example.orderplanning.assembler.WarehouseModelAssembler;
import com.example.orderplanning.controller.WarehouseController;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.WarehouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(WarehouseController.class)
public class WarehouseControllerTest {
    Warehouse warehouse1 = new Warehouse("Minsk", 50, 50);
    Warehouse invalidWarehouse1 = new Warehouse("", 50, 50);
    Warehouse warehouse2 = new Warehouse("Gomel", 100, 0);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private WarehouseService service;
    @MockBean
    private WarehouseModelAssembler assembler;

    @BeforeEach
    public void setUp() {
        Mockito.when(assembler.toModel(warehouse1)).thenReturn(EntityModel.of(warehouse1,
                linkTo(methodOn(WarehouseController.class).one(warehouse1.getId())).withSelfRel(),
                linkTo(methodOn(WarehouseController.class).all()).withRel("warehouses")));
        Mockito.when(assembler.toModel(warehouse2)).thenReturn(EntityModel.of(warehouse2,
                linkTo(methodOn(WarehouseController.class).one(warehouse2.getId())).withSelfRel(),
                linkTo(methodOn(WarehouseController.class).all()).withRel("warehouses")));
    }

    @Test
    public void allWarehousesSuccess() throws Exception {
        Mockito.when(service.findAll()).thenReturn(List.of(warehouse1, warehouse2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/warehouses")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.warehouseList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.warehouseList[0].id", is("Minsk")));
    }

    @Test
    public void newWarehouseSuccess() throws Exception {
        Mockito.doNothing().when(service).saveOrUpdate(warehouse1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(warehouse1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("Minsk")));
    }

    @Test
    public void newWarehouseFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(invalidWarehouse1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("Id is mandatory")));
    }

    @Test
    public void oneWarehouseSuccess() throws Exception {
        Mockito.when(service.findById(warehouse1.getId())).thenReturn(Optional.of(warehouse1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/warehouses/" + warehouse1.getId())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", is(warehouse1.getX())));
    }

    @Test
    public void updateWarehouseSuccess() throws Exception {
        Mockito.doNothing().when(service).saveOrUpdate(warehouse1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/warehouses/"
                        + warehouse1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(mapper.writeValueAsString(warehouse1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("Minsk")));
    }

    @Test
    public void deleteWarehouseSuccess() throws Exception {
        Mockito.when(service.findById(warehouse1.getId())).thenReturn(Optional.of(warehouse1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/warehouses/" + warehouse1.getId())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());
    }
}
