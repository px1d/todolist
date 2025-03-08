package io.github.px1d.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TodoItemStatusEnum {

    TODO(0),
    IN_PROGRESS(1),
    DONE(2);

    private final int value;

    TodoItemStatusEnum(int value) {
        this.value = value;
    }

    @JsonCreator
    public static TodoItemStatusEnum findByInt(int value) {
        for (TodoItemStatusEnum status : TodoItemStatusEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
