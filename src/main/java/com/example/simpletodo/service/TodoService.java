package com.example.simpletodo.service;

import com.example.simpletodo.entity.Todo;
import com.example.simpletodo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Get all todos ordered by creation date (newest first)
     */
    public List<Todo> getAllTodos() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Get todos by completion status
     */
    public List<Todo> getTodosByStatus(Boolean completed) {
        return todoRepository.findByCompletedOrderByCreatedAtDesc(completed);
    }

    /**
     * Get pending (incomplete) todos
     */
    public List<Todo> getPendingTodos() {
        return getTodosByStatus(false);
    }

    /**
     * Get completed todos
     */
    public List<Todo> getCompletedTodos() {
        return getTodosByStatus(true);
    }

    /**
     * Find a todo by ID
     */
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    /**
     * Create a new todo
     */
    public Todo createTodo(String task) {
        if (task == null || task.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        Todo todo = new Todo(task.trim());
        return todoRepository.save(todo);
    }

    /**
     * Create a new todo from Todo object
     */
    public Todo createTodo(Todo todo) {
        if (todo.getTask() == null || todo.getTask().trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        todo.setCompleted(false); // Ensure new todos are not completed
        return todoRepository.save(todo);
    }

    /**
     * Update an existing todo
     */
    public Todo updateTodo(Long id, String task) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            if (task != null && !task.trim().isEmpty()) {
                todo.setTask(task.trim());
            }
            return todoRepository.save(todo);
        }
        throw new RuntimeException("Todo not found with id: " + id);
    }

    /**
     * Toggle completion status of a todo
     */
    public Todo toggleTodoCompletion(Long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setCompleted(!todo.getCompleted());
            return todoRepository.save(todo);
        }
        throw new RuntimeException("Todo not found with id: " + id);
    }

    /**
     * Mark a todo as completed
     */
    public Todo completeTodo(Long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setCompleted(true);
            return todoRepository.save(todo);
        }
        throw new RuntimeException("Todo not found with id: " + id);
    }

    /**
     * Delete a todo by ID
     */
    public void deleteTodo(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Todo not found with id: " + id);
        }
    }

    /**
     * Search todos by task content
     */
    public List<Todo> searchTodos(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllTodos();
        }
        return todoRepository.findByTaskContainingIgnoreCase(searchText.trim());
    }

    /**
     * Get count of todos by completion status
     */
    public long getTodoCount(Boolean completed) {
        return todoRepository.countByCompleted(completed);
    }

    /**
     * Get count of pending todos
     */
    public long getPendingTodoCount() {
        return getTodoCount(false);
    }

    /**
     * Get count of completed todos
     */
    public long getCompletedTodoCount() {
        return getTodoCount(true);
    }

    /**
     * Get total count of all todos
     */
    public long getTotalTodoCount() {
        return todoRepository.count();
    }

    /**
     * Delete all completed todos
     */
    public void deleteCompletedTodos() {
        List<Todo> completedTodos = getCompletedTodos();
        todoRepository.deleteAll(completedTodos);
    }
}
