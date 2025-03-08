package io.github.px1d.repository.impl;

import io.github.px1d.data.req.TodoItemQuery;
import io.github.px1d.data.req.TodoItemSortingOption;
import io.github.px1d.model.TodoItem;
import io.github.px1d.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoItemRepositoryImpl implements TodoItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TodoItem> todoItemRowMapper = (rs, rowNum) -> TodoItem.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .description(rs.getString("description"))
            .status(rs.getInt("status"))
            .dueDate(rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null)
            .todoListId(rs.getLong("todo_list_id")).build();

    @Override
    public Optional<TodoItem> findById(Long id) {
        try {
            String sql = "SELECT * FROM todo_item WHERE id = ?";
            TodoItem todoItem = jdbcTemplate.queryForObject(sql, todoItemRowMapper, id);
            return Optional.ofNullable(todoItem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM todo_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public TodoItem create(TodoItem todoItem) {
        String sql = "INSERT INTO todo_item (title, description, status, due_date, todo_list_id, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, todoItem.getTitle());
            ps.setString(2, todoItem.getDescription());
            ps.setInt(3, todoItem.getStatus());
            ps.setTimestamp(4, todoItem.getDueDate() != null ? Timestamp.valueOf(todoItem.getDueDate()) : null);
            ps.setLong(5, todoItem.getTodoListId());
            ps.setTimestamp(6, Timestamp.valueOf(now));
            ps.setTimestamp(7, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        todoItem.setId(id);
        todoItem.setCreatedAt(now);
        todoItem.setUpdatedAt(now);

        return todoItem;
    }

    @Override
    public TodoItem update(TodoItem todoItem) {
        String sql = "UPDATE todo_item SET title = ?, description = ?, status = ?, due_date = ?, "
                + "todo_list_id = ?, updated_at = ? WHERE id = ?";

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql,
                todoItem.getTitle(), todoItem.getDescription(), todoItem.getStatus(),
                todoItem.getDueDate() != null ? Timestamp.valueOf(todoItem.getDueDate()) : null,
                todoItem.getTodoListId(), Timestamp.valueOf(now), todoItem.getId());

        todoItem.setUpdatedAt(now);
        return todoItem;
    }

    @Override
    public List<TodoItem> queryTodoItems(TodoItemQuery query) {
        StringBuilder sql = new StringBuilder("SELECT * FROM todo_item WHERE todo_list_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(query.getTodoListId());

        if (query.getStatuses() != null && !query.getStatuses().isEmpty()) {
            sql.append(" AND status IN (");
            for (int i = 0; i < query.getStatuses().size(); i++) {
                sql.append(i == 0 ? "?" : ", ?");
                params.add(query.getStatuses().get(i).intValue());
            }
            sql.append(")");
        }

        if (query.getDueDateStart() != null) {
            sql.append(" AND (due_date IS NULL OR due_date >= ?)");
            params.add(Timestamp.valueOf(query.getDueDateStart()));
        }

        if (query.getDueDateEnd() != null) {
            sql.append(" AND (due_date IS NULL OR due_date <= ?)");
            params.add(Timestamp.valueOf(query.getDueDateEnd()));
        }

        // sorting
        if (query.getSorting() != null && !query.getSorting().isEmpty()) {
            sql.append(" ORDER BY ");
            for (int i = 0; i < query.getSorting().size(); i++) {
                TodoItemSortingOption option = query.getSorting().get(i);
                sql.append(option.getField().name());
                if (option.isAscending()) {
                    sql.append(" ASC ");
                } else {
                    sql.append(" DESC ");
                }
                if (i != query.getSorting().size() - 1) {
                    sql.append(" , ");
                }
            }
        }

        Object[] paramsArray = params.toArray();

        return jdbcTemplate.query(sql.toString(), todoItemRowMapper, paramsArray);
    }
}
