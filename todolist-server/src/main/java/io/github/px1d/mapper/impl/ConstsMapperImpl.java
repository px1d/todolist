package io.github.px1d.mapper.impl;

import io.github.px1d.constant.TodoItemStatusEnum;
import io.github.px1d.mapper.ConstsMapper;
import org.springframework.stereotype.Component;

@Component
public class ConstsMapperImpl implements ConstsMapper {

    @Override
    public TodoItemStatusEnum map(int value) {
        return TodoItemStatusEnum.findByInt(value);
    }

    @Override
    public int map(TodoItemStatusEnum status) {
        return status.getValue();
    }
}
