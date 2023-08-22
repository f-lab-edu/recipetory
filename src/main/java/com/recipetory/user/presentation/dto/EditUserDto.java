package com.recipetory.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDto {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,15}$",
            message = "닉네임은 2자 이상 15자 이하의 한글, 영문, 숫자로만 이뤄져야 합니다.")
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private String bio;
}
