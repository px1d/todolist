package io.github.px1d.controller;

import io.github.px1d.auth.annotation.RequiresAuth;
import io.github.px1d.data.req.TodoItemQuery;
import io.github.px1d.data.req.TodoItemSortingOption;
import io.github.px1d.data.vo.TodoItemVO;
import io.github.px1d.service.TodoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/todo-lists/{todoListId:\\d+}/todo-items")
@RequiredArgsConstructor
@Tag(name = "Todo Items")
public class TodoItemController {

    private final TodoItemService todoItemService;

    @Operation(summary = "Query todo items by list ID")
    @GetMapping("/")
    public ResponseEntity<List<TodoItemVO>> queryTodoItems(
            @Parameter @PathVariable Long todoListId,
            @Parameter @RequestParam(required = false) List<Long> statuses,
            @Parameter @RequestParam(required = false) LocalDateTime dueDateStart,
            @Parameter @RequestParam(required = false) LocalDateTime dueDateEnd,
            @Parameter(example = "dueDate+,title-") @RequestParam(required = false, defaultValue = "dueDate+,title+") String sorting
    ) {
        TodoItemQuery query = TodoItemQuery.builder()
                .todoListId(todoListId)
                .statuses(statuses)
                .dueDateStart(dueDateStart)
                .dueDateEnd(dueDateEnd)
                .sorting(TodoItemSortingOption.of(sorting))
                .build();
        return ResponseEntity.ok(todoItemService.queryTodoItems(query));
    }

    @RequiresAuth
    @Operation(summary = "Create a new todo item")
    @PostMapping("/")
    public ResponseEntity<TodoItemVO> createTodoItem(
            @Parameter @PathVariable Long todoListId,
            @Parameter(required = true) @Valid @RequestBody TodoItemVO TodoItemVO) {
        TodoItemVO.setTodoListId(todoListId);
        return new ResponseEntity<>(todoItemService.createTodoItem(TodoItemVO), HttpStatus.CREATED);
    }

    @RequiresAuth
    @Operation(summary = "Update a todo item")
    @PutMapping("/{id}")
    public ResponseEntity<TodoItemVO> updateTodoItem(
            @Parameter @PathVariable Long id,
            @Parameter @PathVariable Long todoListId,
            @Parameter(required = true) @Valid @RequestBody TodoItemVO TodoItemVO) {
        TodoItemVO.setTodoListId(todoListId);
        return ResponseEntity.ok(todoItemService.updateTodoItemWithListCheck(id, todoListId, TodoItemVO));
    }

    @RequiresAuth
    @Operation(summary = "Update todo item completion status")
    @PatchMapping("/{id:\\d+}/status")
    public ResponseEntity<TodoItemVO> updateTodoItemStatus(
            @Parameter @PathVariable Long id,
            @Parameter @PathVariable Long todoListId,
            @Parameter @PathVariable Integer status) {
        return ResponseEntity.ok(todoItemService.updateTodoItemStatus(id, todoListId, status));
    }

    @RequiresAuth
    @Operation(summary = "Delete a todo item")
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteTodoItem(
            @Parameter @PathVariable Long id,
            @Parameter @PathVariable Long todoListId) {
        todoItemService.deleteTodoItemWithListCheck(id, todoListId);
        return ResponseEntity.noContent().build();
    }
}
