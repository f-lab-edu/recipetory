package com.recipetory.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class EditUserDto {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,14}$",
            message = "닉네임은 1자 이상 15자 이하의 한글, 영문, 숫자로만 이뤄져야 합니다.")
    @NotBlank
    @NotNull
    private String name;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        private Long id;
        private String oldName;
        private String newName;
    }
}
