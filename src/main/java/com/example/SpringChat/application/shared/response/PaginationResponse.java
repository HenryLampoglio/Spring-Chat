package com.example.SpringChat.application.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> items;
    private long totalElements;
    private int totalPages;
}