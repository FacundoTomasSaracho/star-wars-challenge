package org.facundosaracho.starwarschallenge.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    private @NotBlank String username;
    private @NotBlank String password;
}
