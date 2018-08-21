package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.ToDo;
import com.example.postgresdemo.repository.ToDoRepository;
import com.example.postgresdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/todos")
    public long getTodos(Pageable pageable) {
        return toDoRepository.findAll(pageable).getTotalElements();
    }

    @PostMapping("/user/{username}/todo")
    public ToDo createTodo(@PathVariable String username, @Valid @RequestBody ToDo toDo) {
        return userRepository.findById(username)
                .map(user -> {
                    toDo.setUser(user);
                    return toDoRepository.save(toDo);
                }).orElseThrow(() -> new ResourceNotFoundException("Username " + username + " not found!"));
    }

    @PutMapping("user/{username}/todo/{todoId}")
    public ToDo updateToDoContent(@PathVariable String username,
                                  @PathVariable Long todoId,
                                  @Valid @RequestBody ToDo toDoRequest) {
        if(!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("Username " + username + " not found!");
        }

        return toDoRepository.findById(todoId)
                .map(todo -> {
                    todo.setTitle(toDoRequest.getTitle());
                    todo.setDescription(toDoRequest.getDescription());
                    return toDoRepository.save(todo);
                }).orElseThrow(() -> new ResourceNotFoundException("Todo with id " + todoId + " not found!"));
    }

    @RequestMapping(value = "/user/{username}/todo", method = RequestMethod.GET)
    public List<ToDo> getToDosByUsername(@PathVariable String username) {
        if(!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("Username " + username + " not found! Cannot display all todo owned by " + username + "!");
        }

        return toDoRepository.findAllByUser_Username(username);
    }

    @DeleteMapping("/user/{username}/todo/{todoId}/delete")
    public ResponseEntity<?> deleteTodo(@PathVariable String username,
                                        @PathVariable Long todoId) {
        if(!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("Username " + username + " not found!");
        }

        return toDoRepository.findById(todoId)
                .map(todo -> {
                    toDoRepository.delete(todo);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Todo with id " + todoId + " not found!"));
    }
}
