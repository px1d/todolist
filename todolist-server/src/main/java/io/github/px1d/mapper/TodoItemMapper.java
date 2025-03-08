package io.github.px1d.mapper;

import io.github.px1d.model.TodoItem;
import io.github.px1d.data.vo.TodoItemVO;

public interface TodoItemMapper {

    TodoItemVO toVO(TodoItem todoItem);

    TodoItem toModel(TodoItemVO todoItemVo);

    void updateTodoItemFromVO(TodoItemVO todoItemVO, TodoItem todoItem);
}
