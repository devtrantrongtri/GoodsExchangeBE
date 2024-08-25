package com.uth.BE.dto.req;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {

    @Min(value = 0, message = "Offset must be greater than or equal to 0")
    private int offset;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int pageSize;

    @NotBlank(message = "Field cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Field can only contain alphanumeric characters and underscores")
    private String field;

    @NotBlank(message = "Order cannot be blank")
    @Pattern(regexp = "^(asc|desc)$", message = "Order must be 'asc' or 'desc'")
    private String order;
}
