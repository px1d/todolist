package io.github.px1d.controller;

import io.github.px1d.auth.annotation.RequiresAuth;
import io.github.px1d.data.vo.TodoListVO;
import io.github.px1d.service.TodoListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/todo-lists")
@RequiredArgsConstructor
@Tag(name = "Todo Lists", description = "API for managing todo lists")
public class TodoListController {

    private final TodoListService todoListService;

    @Operation(summary = "Get all todo lists")
    @GetMapping
    public ResponseEntity<List<TodoListVO>> getAllTodoLists() {
        return ResponseEntity.ok(todoListService.getAllTodoLists());
    }

    @Operation(summary = "Get todo list by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TodoListVO> getTodoListById(
            @Parameter @PathVariable Long id) {
        return ResponseEntity.ok(todoListService.getTodoListById(id));
    }

    @RequiresAuth
    @Operation(summary = "Create a new todo list")
    @PostMapping
    public ResponseEntity<TodoListVO> createTodoList(
            @Parameter @Valid @RequestBody TodoListVO TodoListVO) {
        return new ResponseEntity<>(todoListService.createTodoList(TodoListVO), HttpStatus.CREATED);
    }

    @RequiresAuth
    @Operation(summary = "Update a todo list")
    @PutMapping("/{id}")
    public ResponseEntity<TodoListVO> updateTodoList(
            @Parameter @PathVariable Long id,
            @Parameter(required = true) @Valid @RequestBody TodoListVO TodoListVO) {
        return ResponseEntity.ok(todoListService.updateTodoList(id, TodoListVO));
    }

    @RequiresAuth
    @Operation(summary = "Delete a todo list")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoList(
            @Parameter @PathVariable Long id) {
        todoListService.deleteTodoList(id);
        return ResponseEntity.noContent().build();
    }
}
