package org.example.quizapp.payload;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pageable {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private Object body;
}
