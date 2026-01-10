package com.user.project.ms_user.model.dto.record;

public record ApiResponseDTO<T> (
        boolean success,
        T data,
        String error
){}
