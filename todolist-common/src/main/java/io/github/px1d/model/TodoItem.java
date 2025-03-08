package io.github.px1d.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {

    private Long id;
    private String title;
    private String description;
    private int status;
    private LocalDateTime dueDate;
    private Long todoListId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
