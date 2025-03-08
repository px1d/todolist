package io.github.px1d.data.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemSortingOption {

    private TodoItemSortFieldEnum field;

    private boolean ascending;

    // format 'dueDate+,title-'
    public static List<TodoItemSortingOption> of(String param) {
        if (param == null || param.isEmpty()) {
            return Collections.emptyList();
        }
        String[] options = param.split(",");
        List<TodoItemSortingOption> result = new ArrayList<>();
        for (String option : options) {
            if (StringUtils.isBlank(option)) {
                continue;
            }
            String fieldName = option.substring(0, option.length() - 1);
            TodoItemSortFieldEnum field = TodoItemSortFieldEnum.findNullableByString(fieldName);
            boolean ascending = option.endsWith("+");
            if (field != null) {
                result.add(new TodoItemSortingOption(field, ascending));
            }
        }

        return result;
    }
}
