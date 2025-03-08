package io.github.px1d.repository.impl;

import io.github.px1d.model.TodoList;
import io.github.px1d.repository.TodoListRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoListRepositoryImpl implements TodoListRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TodoList> todoListRowMapper = (rs, rowNum) -> {
        return TodoList.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .userId(rs.getLong("user_id"))
                .build();
    };

    @Override
    public List<TodoList> findAll() {
        String sql = "SELECT * FROM todo_list";
        return jdbcTemplate.query(sql, todoListRowMapper);
    }

    @Override
    public Optional<TodoList> findById(Long id) {
        try {
            String sql = "SELECT * FROM todo_list WHERE id = ?";
            TodoList todoList = jdbcTemplate.queryForObject(sql, todoListRowMapper, id);
            return Optional.ofNullable(todoList);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM todo_list WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public TodoList create(TodoList todoList) {
        String sql = "INSERT INTO todo_list (title, description, user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, todoList.getTitle());
            ps.setString(2, todoList.getDescription());
            ps.setLong(3, todoList.getUserId());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        todoList.setId(id);
        todoList.setCreatedAt(now);
        todoList.setUpdatedAt(now);

        return todoList;
    }

    @Override
    public TodoList update(TodoList todoList) {
        String sql = "UPDATE todo_list SET title = ?, description = ?, updated_at = ? WHERE id = ?";

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql,
                todoList.getTitle(),
                todoList.getDescription(),
                Timestamp.valueOf(now),
                todoList.getId());

        todoList.setUpdatedAt(now);
        return todoList;
    }
}
