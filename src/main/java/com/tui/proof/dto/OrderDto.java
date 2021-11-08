package com.tui.proof.dto;

import com.tui.proof.common.validator.PilotesQuantityInRange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Order data")
@Data
public class OrderDto {

    @Schema(description = "Number of pilotes to cook.",
            example = "5", required = true)
    @PilotesQuantityInRange
    @NotNull
    private Integer pilotes;

    @Schema(description = "Order's number.",
            required = true)
    @NotNull
    @NotEmpty
    private String number;

    @Schema(description = "Order's creation date.",
            required = true)
    @NotNull
    private LocalDateTime createdOn;

    @Schema(description = "Order's total cost.")
    private BigDecimal orderTotal;

    @Schema(description = "Client who registered an order.",
            required = true)
    @NotNull
    @Valid
    private ClientDto client;

    @Schema(description = "Address of registered order.",
            required = true)
    @NotNull
    @Valid
    private AddressDto address;

}