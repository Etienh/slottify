package com.slottify.appointment_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse <T> {

    private List<T> elements;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}
