package com.grupo08.socialmeli.controller;

import com.grupo08.socialmeli.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo08.socialmeli.dto.PostDto;
import com.grupo08.socialmeli.service.IPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    IPostService postService;

    @GetMapping("/products/posts/getAll")
    public ResponseEntity<?> getAllPosts() {
        return new ResponseEntity<>(postService.getAll(),HttpStatus.OK);
    }
    @GetMapping("products/followed/{userId}/list")
    public ResponseEntity<?> followProcducts(@PathVariable Long idUser){
        return new ResponseEntity<>(null);
    }


    @PostMapping("products/post")
    public ResponseEntity<?> insertPost(@RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.insertPost(postDto),HttpStatus.OK);
    }


}
