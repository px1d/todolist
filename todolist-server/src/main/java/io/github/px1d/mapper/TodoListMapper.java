package io.github.px1d.mapper;

import io.github.px1d.model.TodoList;
import io.github.px1d.data.vo.TodoListVO;

public interface TodoListMapper {

    TodoListVO toVO(TodoList todoList);

    TodoList toModel(TodoListVO todoListVo);

    void updateTodoListFromVO(TodoListVO todoListVo, TodoList todoList);
}
