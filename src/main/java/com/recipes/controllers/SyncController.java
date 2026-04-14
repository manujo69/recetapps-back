package com.recipes.controllers;

import com.recipes.dto.SyncPushRequest;
import com.recipes.dto.SyncPushResponse;
import com.recipes.dto.SyncResponse;
import com.recipes.services.SyncService;
import com.recipes.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sync")
@Tag(name = "Sync", description = "Offline sync APIs")
public class SyncController {

    @Autowired
    private SyncService syncService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Pull changes from server. Omit 'since' for full dataset.")
    public ResponseEntity<SyncResponse> pull(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(syncService.pull(userId, since));
    }

    @PostMapping
    @Operation(summary = "Push local changes to server")
    public ResponseEntity<SyncPushResponse> push(
            Authentication authentication,
            @RequestBody SyncPushRequest request) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(syncService.push(userId, request));
    }

    private Long getUserId(Authentication authentication) {
        return userService.getUserIdByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
