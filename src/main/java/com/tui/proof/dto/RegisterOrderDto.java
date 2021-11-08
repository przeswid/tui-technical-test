package com.tui.proof.dto;

import com.tui.proof.common.validator.PilotesQuantityInRange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Schema(description = "Data required to register new order")
@Data
public class RegisterOrderDto {
    @Schema(description = "Number of pilotes to cook.",
            example = "5", required = true)
    @PilotesQuantityInRange
    @NotNull
    private Integer pilotes;

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
