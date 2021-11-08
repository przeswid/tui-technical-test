package com.tui.proof.application.controller;

import com.tui.proof.common.exception.ResourceNotFoundException;
import com.tui.proof.domain.api.OrderService;
import com.tui.proof.dto.OrderDto;
import com.tui.proof.dto.RegisterOrderDto;
import com.tui.proof.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/order")
@Validated
@Tag(name = "order", description = "Order API")
public class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Register new order",
            description = "Register new order",
            tags = {"order"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping(value = "/v1_0",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OrderDto> registerOrder(
            @Parameter(description = "Data required to register an order. Cannot be null",
                    required = true,
                    schema = @Schema(implementation = RegisterOrderDto.class))
            @Valid
            @RequestBody RegisterOrderDto orderDto) {
        Order createdOrder = orderService.createOrder(orderDto);

        return ResponseEntity.created(
                        ServletUriComponentsBuilder.fromPath(
                                "/order/v1_0" + createdOrder.getNumber()).build().toUri())
                .body(new ModelMapper().map(createdOrder, OrderDto.class));
    }

    @Operation(summary = "Get existing order",
            description = "Get existing order",
            tags = {"order"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order founded"),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
            }
    )
    @GetMapping(value = "/v1_0/{number}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OrderDto> getOrder(
            @Parameter(description = "Unique order's number",
                    required = true,
                    schema = @Schema(implementation = String.class))
            @NotNull @NotEmpty
            @PathVariable("number") String number) {
        Order order = orderService.getOrder(number).orElseThrow(() -> new ResourceNotFoundException(number));

        return ResponseEntity.ok(new ModelMapper().map(order, OrderDto.class));
    }


}
