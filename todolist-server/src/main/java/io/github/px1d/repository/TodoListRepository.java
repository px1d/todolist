package io.github.px1d.repository;

import io.github.px1d.model.TodoList;

import java.util.List;
import java.util.Optional;

public interface TodoListRepository {

    List<TodoList> findAll();

    Optional<TodoList> findById(Long id);

    TodoList create(TodoList todoList);

    TodoList update(TodoList todoList);

    void deleteById(Long id);
}
