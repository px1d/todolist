package io.github.px1d.repository;

import io.github.px1d.data.req.TodoItemQuery;
import io.github.px1d.model.TodoItem;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository {

    List<TodoItem> queryTodoItems(TodoItemQuery query);

    Optional<TodoItem> findById(Long id);

    TodoItem create(TodoItem todoItem);

    TodoItem update(TodoItem todoItem);

    void deleteById(Long id);
}
