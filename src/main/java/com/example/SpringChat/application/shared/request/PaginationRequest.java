package com.example.SpringChat.application.shared.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaginationRequest {
    private int page;
    private int size;
}
