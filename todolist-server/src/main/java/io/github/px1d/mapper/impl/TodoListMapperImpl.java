package io.github.px1d.mapper.impl;

import io.github.px1d.model.TodoList;
import io.github.px1d.data.vo.TodoListVO;
import io.github.px1d.mapper.TodoListMapper;
import org.springframework.stereotype.Component;

@Component
public class TodoListMapperImpl implements TodoListMapper {

    @Override
    public TodoListVO toVO(TodoList todoList) {
        if (todoList == null) {
            return null;
        }

        return TodoListVO.builder()
                .id(todoList.getId())
                .title(todoList.getTitle())
                .description(todoList.getDescription())
                .userId(todoList.getUserId())
                .build();
    }

    @Override
    public TodoList toModel(TodoListVO todoListVo) {
        if (todoListVo == null) {
            return null;
        }

        return TodoList.builder()
                .id(todoListVo.getId())
                .title(todoListVo.getTitle())
                .description(todoListVo.getDescription())
                .userId(todoListVo.getUserId())
                .build();
    }

    @Override
    public void updateTodoListFromVO(TodoListVO todoListVo, TodoList todoList) {
        if (todoListVo == null) {
            return;
        }

        if (todoListVo.getTitle() != null) {
            todoList.setTitle(todoListVo.getTitle());
        }
        if (todoListVo.getDescription() != null) {
            todoList.setDescription(todoListVo.getDescription());
        }
        if (todoListVo.getUserId() != null) {
            todoList.setUserId(todoListVo.getUserId());
        }
    }
}
