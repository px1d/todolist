package io.github.px1d.service;

import io.github.px1d.data.req.TodoItemQuery;
import io.github.px1d.data.vo.TodoItemVO;
import io.github.px1d.exception.EntityNotFoundException;
import io.github.px1d.mapper.TodoItemMapper;
import io.github.px1d.model.TodoItem;
import io.github.px1d.repository.TodoItemRepository;
import io.github.px1d.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final TodoListRepository todoListRepository;
    private final TodoItemMapper todoItemMapper;

    @Transactional
    public TodoItemVO createTodoItem(TodoItemVO todoItemVo) {
        TodoItem todoItem = todoItemMapper.toModel(todoItemVo);
        TodoItem savedTodoItem = todoItemRepository.create(todoItem);
        return todoItemMapper.toVO(savedTodoItem);
    }

    @Transactional
    public TodoItemVO updateTodoItemWithListCheck(Long id, Long todoListId, TodoItemVO todoItemVo) {
        TodoItem existingTodoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TodoItem not found with id: " + id));
        todoItemVo.setTodoListId(todoListId);
        todoListRepository.findById(todoListId)
                .orElseThrow(() -> new EntityNotFoundException("TodoList not found with id: " + todoListId));
        todoItemMapper.updateTodoItemFromVO(todoItemVo, existingTodoItem);
        TodoItem updatedTodoItem = todoItemRepository.update(existingTodoItem);
        return todoItemMapper.toVO(updatedTodoItem);
    }

    @Transactional
    public void deleteTodoItemWithListCheck(Long id, Long todoListId) {
        todoItemRepository.deleteById(id);
    }

    @Transactional
    public TodoItemVO updateTodoItemStatus(Long id, Long todoListId, Integer status) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TodoItem not found with id: " + id));
        todoItem.setStatus(status);
        TodoItem updatedTodoItem = todoItemRepository.update(todoItem);
        return todoItemMapper.toVO(updatedTodoItem);
    }

    public List<TodoItemVO> queryTodoItems(TodoItemQuery query) {
        List<TodoItem> todoItems = todoItemRepository.queryTodoItems(query);
        return todoItems.stream().map(todoItemMapper::toVO).collect(Collectors.toList());
    }
}
