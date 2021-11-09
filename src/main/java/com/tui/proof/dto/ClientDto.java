package com.tui.proof.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(description = "Client's data")
@Data
public class ClientDto {

    @Schema(description = "Client's first name.", example = "John", required = true)
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 256, message = "Length of first name should be in range: from 2 to 256")
    private String firstName;

    @Schema(description = "Client's last name.", example = "Smith", required = true)
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 256, message = "Length of last name should be in range: from 2 to 256")
    private String lastName;

    @Schema(description = "Client's phone number", example = "+48123123123", required = true)
    @NotNull(message = "Telephone number cannot be null")
    @Pattern(regexp = "^[0-9\\-+ ]{5,15}$")
    private String telephone;

    @Schema(description = "Client's e-mail", example = "john.smith@domain.com", required = true)
    @NotNull(message = "Email cannot be null")
    @Email
    private String email;
}
