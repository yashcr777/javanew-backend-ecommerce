package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.dto.UserDto;

import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.ForgotPasswordRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{userId}/user")
    @RateLimiter(name="userRateLimiterForId",fallbackMethod = "userServiceFallBackForId")
    public ResponseEntity<ApiResponse>getUserById(@PathVariable Long userId){
        try {
            User user=userService.getUserById(userId);
            UserDto userDto=userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success",user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    public ResponseEntity<ApiResponse>userServiceFallBackForId(Long userId,Exception e){
        User user=new User();
        user.setId(1234L);
        user.setEmail("dummy@gmail.com");
        user.setFirstName("Dummy");
        user.setLastName("Agarwal");
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Service is down",user));
    }




//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("all")
    @RateLimiter(name="userRateLimiter",fallbackMethod = "userServiceFallBack")
    public ResponseEntity<ApiResponse>getAllUser(){
        try {
            List<User>users=userService.getAllUser();
            return ResponseEntity.ok(new ApiResponse("Success",users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    public ResponseEntity<ApiResponse>userServiceFallBack(Exception e){

        log.info("Fallback is executed because service is down: ",e.getMessage());
        List<User>users=new ArrayList<>();
        User user=new User();
        user.setId(1234L);
        user.setEmail("dummy@gmail.com");
        user.setFirstName("Dummy");
        user.setLastName("Agarwal");
        users.add(user);
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Service is down",users));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse>createUser(@RequestBody @Validated CreateUserRequest request)
    {
        try {
            User user=userService.createUser(request);
            UserDto userDto=userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success",userDto));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage()+"we",null));
        }
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<ApiResponse>forgotPassword(@RequestBody ForgotPasswordRequest request){
        try{
            User user=userService.forgotPassword(request);
            UserDto userDto=userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Successfully updated Password",userDto));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse>updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId)
    {
        try {
            User user=userService.updateUser(request,userId);
            UserDto userDto=userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Successfully updated User",userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse>deleteUser(@PathVariable Long userId)
    {
        try {
            userService.deletedUser(userId);
            return ResponseEntity.ok(new ApiResponse("Successfully Deleted User",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/sort/{field}")
    public List<User>sortUsers(@PathVariable String field){
        return userService.sortByField(field);
    }
    @GetMapping("/sortdesc/{field}")
    public List<User>sortUsersByDesc(@PathVariable String field){
        return userService.sortByFieldDesc(field);
    }
    @GetMapping("/pagination")
    public List<User> userPagination(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize){
        return userService.getUserByPagination(offset,pageSize).getContent();
    }
    @GetMapping("/paginationAndSorting/{field}")
    public List<User> userPaginationAndSorting(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize,@PathVariable String field){
        return userService.getUserByPaginationAndSorting(offset,pageSize,field).getContent();
    }
}