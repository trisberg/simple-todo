package com.example.simpletodo.controller;

import com.example.simpletodo.entity.Todo;
import com.example.simpletodo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoRestController {

    private final TodoService todoService;

    @Autowired
    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Get all todos
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    /**
     * Get todos by status
     */
    @GetMapping("/status/{completed}")
    public ResponseEntity<List<Todo>> getTodosByStatus(@PathVariable Boolean completed) {
        List<Todo> todos = todoService.getTodosByStatus(completed);
        return ResponseEntity.ok(todos);
    }

    /**
     * Get pending todos
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Todo>> getPendingTodos() {
        List<Todo> todos = todoService.getPendingTodos();
        return ResponseEntity.ok(todos);
    }

    /**
     * Get completed todos
     */
    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> getCompletedTodos() {
        List<Todo> todos = todoService.getCompletedTodos();
        return ResponseEntity.ok(todos);
    }

    /**
     * Get a todo by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new todo
     */
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        try {
            Todo createdTodo = todoService.createTodo(todo);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update a todo
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo todo) {
        try {
            Todo updatedTodo = todoService.updateTodo(id, todo.getTask());
            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Toggle todo completion status
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleTodoCompletion(@PathVariable Long id) {
        try {
            Todo updatedTodo = todoService.toggleTodoCompletion(id);
            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mark todo as completed
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Todo> completeTodo(@PathVariable Long id) {
        try {
            Todo updatedTodo = todoService.completeTodo(id);
            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a todo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        try {
            todoService.deleteTodo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search todos
     */
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodos(@RequestParam String q) {
        List<Todo> todos = todoService.searchTodos(q);
        return ResponseEntity.ok(todos);
    }

    /**
     * Get todo statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<TodoStats> getTodoStats() {
        TodoStats stats = new TodoStats(
            todoService.getTotalTodoCount(),
            todoService.getPendingTodoCount(),
            todoService.getCompletedTodoCount()
        );
        return ResponseEntity.ok(stats);
    }

    /**
     * Delete all completed todos
     */
    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedTodos() {
        todoService.deleteCompletedTodos();
        return ResponseEntity.noContent().build();
    }

    /**
     * Inner class for todo statistics
     */
    public static class TodoStats {
        private long total;
        private long pending;
        private long completed;

        public TodoStats(long total, long pending, long completed) {
            this.total = total;
            this.pending = pending;
            this.completed = completed;
        }

        // Getters
        public long getTotal() { return total; }
        public long getPending() { return pending; }
        public long getCompleted() { return completed; }

        // Setters
        public void setTotal(long total) { this.total = total; }
        public void setPending(long pending) { this.pending = pending; }
        public void setCompleted(long completed) { this.completed = completed; }
    }
}
