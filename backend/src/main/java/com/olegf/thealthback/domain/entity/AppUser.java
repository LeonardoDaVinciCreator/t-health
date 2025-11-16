package com.olegf.thealthback.domain.entity;

import com.olegf.thealthback.web.dto.UserCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Base64;
import java.util.Collections;
import java.util.Set;

@Data
@Table("app_users")
@AllArgsConstructor
public class AppUser {
    @Id
    private Long id;
    private String username;
    private byte[] phone;

    @MappedCollection(idColumn = "user_id")
    private Set<Activity> activities;

    public String getPhone() {
        return new String(Base64.getDecoder().decode(
                Base64.getDecoder().decode(phone)
        ));
    }

    public static AppUser from(UserCreateDto createDto) {
        //TODO: add some cryptography
        var encodedPhone = Base64.getEncoder().encode(
                Base64.getEncoder().encode(createDto.getPhone().getBytes())
        );

        return new AppUser(
                null,
                createDto.getUsername(),
                encodedPhone,
                Collections.emptySet()
        );
    }
}
