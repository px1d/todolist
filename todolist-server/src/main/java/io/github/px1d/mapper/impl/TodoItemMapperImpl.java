package io.github.px1d.mapper.impl;

import io.github.px1d.model.TodoItem;
import io.github.px1d.data.vo.TodoItemVO;
import io.github.px1d.mapper.ConstsMapper;
import io.github.px1d.mapper.TodoItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoItemMapperImpl implements TodoItemMapper {

    @Autowired
    private ConstsMapper constsMapper;

    @Override
    public TodoItemVO toVO(TodoItem todoItem) {
        if (todoItem == null) {
            return null;
        }

        return TodoItemVO.builder()
                .id(todoItem.getId())
                .title(todoItem.getTitle())
                .description(todoItem.getDescription())
                .status(constsMapper.map(todoItem.getStatus()))
                .dueDate(todoItem.getDueDate())
                .todoListId(todoItem.getTodoListId())
                .build();
    }

    @Override
    public TodoItem toModel(TodoItemVO todoItemVo) {
        if (todoItemVo == null) {
            return null;
        }

        return TodoItem.builder()
                .id(todoItemVo.getId())
                .title(todoItemVo.getTitle())
                .description(todoItemVo.getDescription())
                .status(todoItemVo.getStatus() != null ? constsMapper.map(todoItemVo.getStatus()) : 0)
                .dueDate(todoItemVo.getDueDate())
                .todoListId(todoItemVo.getTodoListId())
                .build();
    }

    @Override
    public void updateTodoItemFromVO(TodoItemVO todoItemVO, TodoItem todoItem) {
        if (todoItemVO == null) {
            return;
        }

        if (todoItemVO.getTitle() != null) {
            todoItem.setTitle(todoItemVO.getTitle());
        }
        if (todoItemVO.getDescription() != null) {
            todoItem.setDescription(todoItemVO.getDescription());
        }
        if (todoItemVO.getStatus() != null) {
            todoItem.setStatus(constsMapper.map(todoItemVO.getStatus()));
        }
        if (todoItemVO.getDueDate() != null) {
            todoItem.setDueDate(todoItemVO.getDueDate());
        }
        if (todoItemVO.getTodoListId() != null) {
            todoItem.setTodoListId(todoItemVO.getTodoListId());
        }
    }
}
