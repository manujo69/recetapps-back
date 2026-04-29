package com.recipes.controllers;

import com.recipes.dto.SyncRequest;
import com.recipes.dto.SyncResponse;
import com.recipes.dto.SyncResult;
import com.recipes.services.SyncService;
import com.recipes.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
@Tag(name = "Sync", description = "Offline-first sync APIs")
public class SyncController {

    @Autowired
    private SyncService syncService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get full data snapshot for the current user")
    public ResponseEntity<SyncResponse> getSync(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(syncService.getSync(userId));
    }

    @PostMapping
    @Operation(summary = "Push local changes and get server-assigned IDs")
    public ResponseEntity<SyncResult> postSync(@RequestBody SyncRequest request,
                                               Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(syncService.processSync(request, userId));
    }

    private Long getUserId(Authentication authentication) {
        return userService.getUserIdByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
