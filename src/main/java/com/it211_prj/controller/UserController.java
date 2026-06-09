package com.it211_prj.controller;

import com.it211_prj.dto.user.UserRequest;
import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Admin xem danh sach tat ca tai khoan.
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    // Admin xem chi tiet mot tai khoan.
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // Admin tao user voi role bat ky: ADMIN, LECTURER, STUDENT.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    // Admin cap nhat profile, role, trang thai va mat khau user.
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    // Admin xoa user khi khong con can quan ly.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
