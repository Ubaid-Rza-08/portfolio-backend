package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World - Simplified User API";
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Find user by ID - returns list for consistency
    @GetMapping("/id/{id}")
    public ResponseEntity<List<User>> getUserById(@PathVariable UUID id) {
        List<User> users = userService.findById(id);
        return ResponseEntity.ok(users);
    }

    // Find user by name (partial match)
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String name) {
        List<User> users = userService.findByNameContaining(name);
        return ResponseEntity.ok(users);
    }

    // Find user by title (partial match)
    @GetMapping("/title/{title}")
    public ResponseEntity<List<User>> getUserByTitle(@PathVariable String title) {
        List<User> users = userService.findByTitleContaining(title);
        return ResponseEntity.ok(users);
    }

    // Create new user with form data
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        try {
            User user = new User();
            user.setName(name);
            user.setTitle(title);
            user.setDescription(description);

            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Create user with JSON
    @PostMapping("/json")
    public ResponseEntity<?> createUserJson(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Update user with JSON
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Update user with form data
    @PutMapping(value = "/{id}/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserWithForm(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        try {
            Optional<User> existingUserOpt = userService.findByIdOptional(id);
            if (existingUserOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found with id: " + id);
                return ResponseEntity.notFound().build();
            }

            User user = existingUserOpt.get();

            // Update fields if provided
            if (name != null && !name.trim().isEmpty()) user.setName(name);
            if (title != null && !title.trim().isEmpty()) user.setTitle(title);
            if (description != null && !description.trim().isEmpty()) user.setDescription(description);

            User updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            if (userService.deleteUser(id)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User deleted successfully");
                return ResponseEntity.ok(response);
            }
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found with id: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Get user count
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        try {
            long count = userService.getUsersCount();
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Check if user exists
    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> userExists(@PathVariable UUID id) {
        try {
            boolean exists = userService.existsById(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get single user by ID (returns User object, not list)
    @GetMapping("/single/{id}")
    public ResponseEntity<?> getSingleUserById(@PathVariable UUID id) {
        try {
            Optional<User> userOpt = userService.findByIdOptional(id);
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(userOpt.get());
            }
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found with id: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Search users by keyword (searches both name and title)
//    @GetMapping("/search/{keyword}")
//    public ResponseEntity<List<User>> searchUsers(@PathVariable String keyword) {
//        try {
//            List<User> users = userService.searchUsers(keyword);
//            return ResponseEntity.ok(users);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // Get API info
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /", "Get all users");
        endpoints.put("GET /id/{id}", "Get user by ID (returns list)");
        endpoints.put("GET /single/{id}", "Get single user by ID (returns user object)");
        endpoints.put("GET /name/{name}", "Search users by name");
        endpoints.put("GET /title/{title}", "Search users by title");
        endpoints.put("GET /search/{keyword}", "Search users by keyword (name or title)");
        endpoints.put("POST / (form-data)", "Create user with form data");
        endpoints.put("POST /json", "Create user with JSON");
        endpoints.put("PUT /{id}", "Update user with JSON");
        endpoints.put("PUT /{id}/form", "Update user with form data");
        endpoints.put("DELETE /{id}", "Delete user");
        endpoints.put("GET /count", "Get total user count");
        endpoints.put("GET /exists/{id}", "Check if user exists");

        info.put("endpoints", endpoints);
        info.put("baseUrl", "/api/users");
        info.put("description", "Simplified User API with only basic fields: id, name, title, description");
        info.put("version", "1.0");

        return ResponseEntity.ok(info);
    }
}