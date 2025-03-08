package io.github.px1d.data.req;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum TodoItemSortFieldEnum {

    DUE_DATE(1, "dueDate"),
    TITLE(2, "title"),
    STATUS(3, "status"),
    ;

    private final int value;

    private final String name;

    TodoItemSortFieldEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Optional<TodoItemSortFieldEnum> findByInt(int value) {
        for (TodoItemSortFieldEnum item : TodoItemSortFieldEnum.values()) {
            if (item.value == value) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    @JsonCreator
    public static TodoItemSortFieldEnum findNullableByInt(int value) {
        for (TodoItemSortFieldEnum item : TodoItemSortFieldEnum.values()) {
            if (item.value == value) {
                return item;
            }
        }

        return null;
    }

    public static Optional<TodoItemSortFieldEnum> findByString(String name) {
        for (TodoItemSortFieldEnum item : TodoItemSortFieldEnum.values()) {
            if (item.name.equals(name)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    public static TodoItemSortFieldEnum findNullableByString(String name) {
        for (TodoItemSortFieldEnum item : TodoItemSortFieldEnum.values()) {
            if (item.name.equals(name)) {
                return item;
            }
        }

        return null;
    }

    public String toString() {
        return this.name;
    }

    @JsonValue
    public int toInt() {
        return this.value;
    }
}


