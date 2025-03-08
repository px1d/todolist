package io.github.px1d.service;

import io.github.px1d.exception.EntityNotFoundException;
import io.github.px1d.model.TodoList;
import io.github.px1d.data.vo.TodoListVO;
import io.github.px1d.mapper.TodoListMapper;
import io.github.px1d.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final TodoListMapper todoListMapper;

    public List<TodoListVO> getAllTodoLists() {
        return todoListRepository.findAll().stream()
                .map(todoListMapper::toVO)
                .collect(Collectors.toList());
    }

    public TodoListVO getTodoListById(Long id) {
        TodoList todoList = todoListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TodoList not found with id: " + id));
        return todoListMapper.toVO(todoList);
    }

    @Transactional
    public TodoListVO createTodoList(TodoListVO todoListVo) {
        TodoList todoList = todoListMapper.toModel(todoListVo);
        TodoList savedTodoList = todoListRepository.create(todoList);
        return todoListMapper.toVO(savedTodoList);
    }

    @Transactional
    public TodoListVO updateTodoList(Long id, TodoListVO todoListVo) {
        TodoList existingTodoList = todoListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TodoList not found with id: " + id));
        todoListMapper.updateTodoListFromVO(todoListVo, existingTodoList);
        TodoList updatedTodoList = todoListRepository.update(existingTodoList);
        return todoListMapper.toVO(updatedTodoList);
    }

    @Transactional
    public void deleteTodoList(Long id) {
        todoListRepository.deleteById(id);
    }
}
