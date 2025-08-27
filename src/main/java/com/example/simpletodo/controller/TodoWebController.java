package com.example.simpletodo.controller;

import com.example.simpletodo.entity.Todo;
import com.example.simpletodo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/")
public class TodoWebController {

    private final TodoService todoService;

    @Autowired
    public TodoWebController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Display the main todo list page
     */
    @GetMapping
    public String index(Model model, @RequestParam(required = false) String filter, 
                       @RequestParam(required = false) String search) {
        List<Todo> todos;
        
        if (search != null && !search.trim().isEmpty()) {
            todos = todoService.searchTodos(search);
            model.addAttribute("searchQuery", search);
        } else if ("completed".equals(filter)) {
            todos = todoService.getCompletedTodos();
            model.addAttribute("currentFilter", "completed");
        } else if ("pending".equals(filter)) {
            todos = todoService.getPendingTodos();
            model.addAttribute("currentFilter", "pending");
        } else {
            todos = todoService.getAllTodos();
            model.addAttribute("currentFilter", "all");
        }

        model.addAttribute("todos", todos);
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("totalCount", todoService.getTotalTodoCount());
        model.addAttribute("pendingCount", todoService.getPendingTodoCount());
        model.addAttribute("completedCount", todoService.getCompletedTodoCount());

        return "index";
    }

    /**
     * Create a new todo
     */
    @PostMapping("/todos")
    public String createTodo(@Valid @ModelAttribute("newTodo") Todo todo, 
                           BindingResult bindingResult, 
                           RedirectAttributes redirectAttributes, 
                           Model model) {
        if (bindingResult.hasErrors()) {
            // Re-populate the model with necessary data
            List<Todo> todos = todoService.getAllTodos();
            model.addAttribute("todos", todos);
            model.addAttribute("totalCount", todoService.getTotalTodoCount());
            model.addAttribute("pendingCount", todoService.getPendingTodoCount());
            model.addAttribute("completedCount", todoService.getCompletedTodoCount());
            model.addAttribute("currentFilter", "all");
            return "index";
        }

        try {
            todoService.createTodo(todo);
            redirectAttributes.addFlashAttribute("successMessage", "Todo created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating todo: " + e.getMessage());
        }

        return "redirect:/";
    }

    /**
     * Toggle todo completion status
     */
    @PostMapping("/todos/{id}/toggle")
    public String toggleTodo(@PathVariable Long id, RedirectAttributes redirectAttributes,
                           @RequestParam(required = false) String filter) {
        try {
            todoService.toggleTodoCompletion(id);
            redirectAttributes.addFlashAttribute("successMessage", "Todo updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating todo: " + e.getMessage());
        }

        String redirectUrl = "/";
        if (filter != null && !filter.isEmpty()) {
            redirectUrl += "?filter=" + filter;
        }
        return "redirect:" + redirectUrl;
    }

    /**
     * Delete a todo
     */
    @PostMapping("/todos/{id}/delete")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes,
                           @RequestParam(required = false) String filter) {
        try {
            todoService.deleteTodo(id);
            redirectAttributes.addFlashAttribute("successMessage", "Todo deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting todo: " + e.getMessage());
        }

        String redirectUrl = "/";
        if (filter != null && !filter.isEmpty()) {
            redirectUrl += "?filter=" + filter;
        }
        return "redirect:" + redirectUrl;
    }

    /**
     * Edit todo page
     */
    @GetMapping("/todos/{id}/edit")
    public String editTodoForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Todo todo = todoService.getTodoById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found"));
            model.addAttribute("todo", todo);
            return "edit";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Todo not found");
            return "redirect:/";
        }
    }

    /**
     * Update a todo
     */
    @PostMapping("/todos/{id}/edit")
    public String updateTodo(@PathVariable Long id, @Valid @ModelAttribute("todo") Todo todo,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todo", todo);
            return "edit";
        }

        try {
            todoService.updateTodo(id, todo.getTask());
            redirectAttributes.addFlashAttribute("successMessage", "Todo updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating todo: " + e.getMessage());
        }

        return "redirect:/";
    }

    /**
     * Delete all completed todos
     */
    @PostMapping("/todos/delete-completed")
    public String deleteCompletedTodos(RedirectAttributes redirectAttributes) {
        try {
            todoService.deleteCompletedTodos();
            redirectAttributes.addFlashAttribute("successMessage", "All completed todos deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting completed todos: " + e.getMessage());
        }

        return "redirect:/";
    }
}
