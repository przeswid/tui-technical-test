package com.tui.proof.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "Address data")
@Data
public class AddressDto {

    @Schema(description = "Street name.", example = "Great Scotland Yard", required = true)
    @NotNull(message = "Street cannot be null")
    @Size(min = 2, max = 256, message = "Length of street should be in range: from 2 to 256")
    private String street;

    @Schema(description = "Address postcode.", example = "E1 7AY", required = true)
    @NotNull(message = "Postcode cannot be null")
    @Size(min = 2, max = 64, message = "Length of post code should be in range: from 2 to 64")
    private String postcode;

    @Schema(description = "Address city.", example = "London", required = true)
    @NotNull(message = "City cannot be null")
    @Size(min = 2, max = 256, message = "Length of city should be in range: from 2 to 64")
    private String city;

    @Schema(description = "Address house number.", example = "12A", required = true)
    @NotNull(message = "House number cannot be null")
    @Size(min = 1, max = 16, message = "Length of house number should be in range: from 1 to 16")
    private String houseNumber;

    @Schema(description = "Address flat number.", example = "1")
    @Size(min = 1, max = 16, message = "Length of flat number should be in range: from 1 to 16")
    private String flatNumber;

    @Schema(description = "Address country name.", example = "England", required = true)
    @NotNull(message = "Country cannot be null")
    @Size(min = 2, max = 256, message = "Length of country should be in range: from 2 to 32")
    private String country;
}
