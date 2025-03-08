package io.github.px1d.mapper;

import io.github.px1d.constant.TodoItemStatusEnum;

public interface ConstsMapper {

    TodoItemStatusEnum map(int value);

    int map(TodoItemStatusEnum status);
}
