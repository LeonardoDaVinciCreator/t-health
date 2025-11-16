package com.olegf.thealthback.domain.service;

import com.olegf.thealthback.domain.EntityNotFoundException;
import com.olegf.thealthback.domain.entity.Activity;
import com.olegf.thealthback.domain.entity.AppUser;
import com.olegf.thealthback.domain.repository.ActivityRepo;
import com.olegf.thealthback.domain.repository.UserRepo;
import com.olegf.thealthback.web.dto.ActivityCreateDto;
import com.olegf.thealthback.web.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ActivityRepo activityRepo;

    public AppUser create(UserCreateDto createDto) {
        var user = AppUser.from(createDto);
        return userRepo.save(user);
    }

    public Activity createActivity(long userId, ActivityCreateDto activityCreateDto) {
        var activity = Activity.from(activityCreateDto);
        activity.setUserId(userId);
        activity.setDate(LocalDateTime.now());
        return activityRepo.save(activity);
    }

    @Transactional(readOnly = true)
    public AppUser getUserById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: %s not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public List<Activity> getUserActivities(long userId, LocalDateTime date) {
        if (date == null) return activityRepo.findByUserId(userId);

        LocalDateTime start = date.toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        return activityRepo.findByDate(userId, start, end);
    }

    @Transactional(readOnly = true)
    public List<AppUser> getUsers() {
        return (List<AppUser>) userRepo.findAll();
    }
}
