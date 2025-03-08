package io.github.px1d.service;

import io.github.px1d.data.vo.TodoListVO;
import io.github.px1d.exception.EntityNotFoundException;
import io.github.px1d.mapper.TodoListMapper;
import io.github.px1d.model.TodoList;
import io.github.px1d.repository.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private TodoListMapper todoListMapper;

    @InjectMocks
    private TodoListService todoListService;

    private TodoList todoList1;
    private TodoList todoList2;
    private TodoListVO todoListVO1;
    private TodoListVO todoListVO2;

    @BeforeEach
    void setUp() {
        // Setup test data
        todoList1 = TodoList.builder()
                .id(1L)
                .title("Shopping List")
                .description("Groceries for the week")
                .userId(101L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        todoList2 = TodoList.builder()
                .id(2L)
                .title("Work Tasks")
                .description("Tasks for this sprint")
                .userId(101L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        todoListVO1 = TodoListVO.builder()
                .id(1L)
                .title("Shopping List")
                .description("Groceries for the week")
                .userId(101L)
                .build();

        todoListVO2 = TodoListVO.builder()
                .id(2L)
                .title("Work Tasks")
                .description("Tasks for this sprint")
                .userId(101L)
                .build();
    }

    @Test
    @DisplayName("getAllTodoLists should return all todo lists")
    void getAllTodoListsShouldReturnAllTodoLists() {
        // Arrange
        when(todoListRepository.findAll()).thenReturn(Arrays.asList(todoList1, todoList2));
        when(todoListMapper.toVO(todoList1)).thenReturn(todoListVO1);
        when(todoListMapper.toVO(todoList2)).thenReturn(todoListVO2);

        // Act
        List<TodoListVO> result = todoListService.getAllTodoLists();

        // Assert
        assertEquals(2, result.size(), "Should return 2 todo lists");
        assertEquals(todoListVO1, result.get(0), "First todo list should match");
        assertEquals(todoListVO2, result.get(1), "Second todo list should match");
        verify(todoListRepository, times(1)).findAll();
        verify(todoListMapper, times(1)).toVO(todoList1);
        verify(todoListMapper, times(1)).toVO(todoList2);
    }

    @Test
    @DisplayName("getTodoListById should return a todo list when it exists")
    void getTodoListByIdShouldReturnTodoListWhenExists() {
        // Arrange
        when(todoListRepository.findById(1L)).thenReturn(Optional.of(todoList1));
        when(todoListMapper.toVO(todoList1)).thenReturn(todoListVO1);

        // Act
        TodoListVO result = todoListService.getTodoListById(1L);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(todoListVO1, result, "Should return the expected TodoListVO");
        verify(todoListRepository, times(1)).findById(1L);
        verify(todoListMapper, times(1)).toVO(todoList1);
    }

    @Test
    @DisplayName("getTodoListById should throw EntityNotFoundException when todo list does not exist")
    void getTodoListByIdShouldThrowExceptionWhenNotExists() {
        // Arrange
        when(todoListRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> todoListService.getTodoListById(999L),
                "Should throw EntityNotFoundException for non-existent todo list");
        verify(todoListRepository, times(1)).findById(999L);
        verify(todoListMapper, never()).toVO(any());
    }

    @Test
    @DisplayName("createTodoList should create and return the new todo list")
    void createTodoListShouldCreateAndReturnNewTodoList() {
        // Arrange
        TodoListVO newTodoListVO = TodoListVO.builder()
                .title("New List")
                .description("New Description")
                .userId(101L)
                .build();

        TodoList newTodoList = TodoList.builder()
                .title("New List")
                .description("New Description")
                .userId(101L)
                .build();

        TodoList savedTodoList = TodoList.builder()
                .id(3L)
                .title("New List")
                .description("New Description")
                .userId(101L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TodoListVO savedTodoListVO = TodoListVO.builder()
                .id(3L)
                .title("New List")
                .description("New Description")
                .userId(101L)
                .build();

        when(todoListMapper.toModel(newTodoListVO)).thenReturn(newTodoList);
        when(todoListRepository.create(newTodoList)).thenReturn(savedTodoList);
        when(todoListMapper.toVO(savedTodoList)).thenReturn(savedTodoListVO);

        // Act
        TodoListVO result = todoListService.createTodoList(newTodoListVO);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(savedTodoListVO, result, "Should return the saved TodoListVO");
        assertEquals(3L, result.getId(), "ID should be set in the returned VO");
        verify(todoListMapper, times(1)).toModel(newTodoListVO);
        verify(todoListRepository, times(1)).create(newTodoList);
        verify(todoListMapper, times(1)).toVO(savedTodoList);
    }

    @Test
    @DisplayName("updateTodoList should update and return the todo list when it exists")
    void updateTodoListShouldUpdateAndReturnTodoListWhenExists() {
        // Arrange
        Long id = 1L;
        TodoListVO updateTodoListVO = TodoListVO.builder()
                .title("Updated Shopping List")
                .description("Updated groceries list")
                .userId(101L)
                .build();

        TodoList existingTodoList = todoList1;
        TodoList updatedTodoList = TodoList.builder()
                .id(1L)
                .title("Updated Shopping List")
                .description("Updated groceries list")
                .userId(101L)
                .createdAt(todoList1.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        TodoListVO updatedTodoListVO = TodoListVO.builder()
                .id(1L)
                .title("Updated Shopping List")
                .description("Updated groceries list")
                .userId(101L)
                .build();

        when(todoListRepository.findById(id)).thenReturn(Optional.of(existingTodoList));
        doNothing().when(todoListMapper).updateTodoListFromVO(updateTodoListVO, existingTodoList);
        when(todoListRepository.update(existingTodoList)).thenReturn(updatedTodoList);
        when(todoListMapper.toVO(updatedTodoList)).thenReturn(updatedTodoListVO);

        // Act
        TodoListVO result = todoListService.updateTodoList(id, updateTodoListVO);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(updatedTodoListVO, result, "Should return the updated TodoListVO");
        verify(todoListRepository, times(1)).findById(id);
        verify(todoListMapper, times(1)).updateTodoListFromVO(updateTodoListVO, existingTodoList);
        verify(todoListRepository, times(1)).update(existingTodoList);
        verify(todoListMapper, times(1)).toVO(updatedTodoList);
    }

    @Test
    @DisplayName("updateTodoList should throw EntityNotFoundException when todo list does not exist")
    void updateTodoListShouldThrowExceptionWhenNotExists() {
        // Arrange
        Long id = 999L;
        TodoListVO updateTodoListVO = TodoListVO.builder()
                .title("Updated Title")
                .description("Updated Description")
                .userId(101L)
                .build();

        when(todoListRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> todoListService.updateTodoList(id, updateTodoListVO),
                "Should throw EntityNotFoundException for non-existent todo list");
        verify(todoListRepository, times(1)).findById(id);
        verify(todoListMapper, never()).updateTodoListFromVO(any(), any());
        verify(todoListRepository, never()).update(any());
        verify(todoListMapper, never()).toVO(any());
    }

    @Test
    @DisplayName("deleteTodoList should delete the todo list")
    void deleteTodoListShouldDeleteTodoList() {
        // Arrange
        Long id = 1L;
        doNothing().when(todoListRepository).deleteById(id);

        // Act
        todoListService.deleteTodoList(id);

        // Assert
        verify(todoListRepository, times(1)).deleteById(id);
    }
}
