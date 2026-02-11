package com.example.SpringChat.application.shared.response;


import java.util.List;
import java.util.function.Function;

public record PaginationResponseDTO<T>(List<T> items,  int page, int totalPages) {

    public <R> PaginationResponseDTO<R> map(Function<T, R> mapper) {
        List<R> mappedItems = this.items.stream().map(mapper).toList();
        return new PaginationResponseDTO<>(mappedItems, page, totalPages);
    }
}