package com.internal.tasktracker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Validate pagination values
        if (page < 1) {
            return ResponseEntity.badRequest()
                    .body("Page number must be greater than or equal to 1.");
        }

        if (pageSize < 1 || pageSize > 100) {
            return ResponseEntity.badRequest()
                    .body("Page size must be between 1 and 100.");
        }

        // Normalize query input
        String query = q == null ? "" : q.trim();
        String searchTerm = "%" + query.toLowerCase(Locale.ROOT) + "%";

        // Parse and validate status filter
        String normalizedStatus = null;

        if (status != null && !status.trim().isEmpty()) {
            try {
                normalizedStatus = TaskStatus
                        .valueOf(status.trim().toUpperCase(Locale.ROOT))
                        .name();
            } catch (IllegalArgumentException ex) {
                 return ResponseEntity.badRequest().body(
                 Map.of("error", "Invalid task status")
                );
            }
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // Fetch matching tasks
        Page<Task> taskPage =
                taskRepository.searchTasks(searchTerm, normalizedStatus, pageable);


        // Prepare response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", taskPage.getContent());
        response.put("total", taskPage.getTotalElements());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }
}