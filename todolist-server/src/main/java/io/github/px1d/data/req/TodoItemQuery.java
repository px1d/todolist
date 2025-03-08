package io.github.px1d.data.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemQuery {

    @NotBlank(message = "TodoListId is required")
    private Long todoListId;

    private List<Long> statuses;

    private LocalDateTime dueDateStart;

    private LocalDateTime dueDateEnd;
    
    private List<TodoItemSortingOption> sorting;
}
