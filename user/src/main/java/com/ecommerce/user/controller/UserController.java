package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    //Another way request
    @RequestMapping( method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        List<UserResponse> userList = userService.fetchAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){

        log.info("Fetching user with id {}", id);

        return  userService.fetchUser(id)
                .map(ResponseEntity :: ok)
                .orElseGet(() ->  new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){

         userService.addUser(userRequest);

        return new ResponseEntity<>("User added Successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest userRequest, @PathVariable String id){

        boolean updated = userService.updateUser(userRequest,id);
        if(updated){
            return new ResponseEntity<>("Updated Successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not Updated", HttpStatus.NOT_FOUND);
        }



    }

}
