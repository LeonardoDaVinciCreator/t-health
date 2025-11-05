package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.domain.entity.Activity;
import com.olegf.thealthback.domain.entity.AppUser;
import com.olegf.thealthback.domain.service.UserService;
import com.olegf.thealthback.web.dto.ActivityCreateDto;
import com.olegf.thealthback.web.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public AppUser createUser(@RequestBody UserCreateDto createDto) {
        return userService.create(createDto);
    }

    @PostMapping("/{userId}/activities")
    public Activity createActivity(@PathVariable long userId, @RequestBody ActivityCreateDto createDto) {
        return userService.createActivity(userId, createDto);
    }

    @GetMapping
    public List<AppUser> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{userId}/activities")
    public Page<Activity> getUserActivities(
            @PathVariable long userId,
            @RequestParam(value = "date", required = false) LocalDateTime date, Pageable pageable
    ) {
        return userService.getUserActivities(userId, date, pageable);
    }
}
