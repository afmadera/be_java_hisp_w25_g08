package com.grupo08.socialmeli.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.grupo08.socialmeli.entity.Post;
import com.grupo08.socialmeli.entity.Product;

@Repository
public class PostRepositoryImp implements IPostRepository {

    @Autowired
    ResourceLoader resourceLoader;

    List<Post> listPosts = new ArrayList<>() {{
        add(new Post(
            123, 
            "19-02-2024", 
            new Product(
                123, 
                "Silla Gamer", 
                "Gamer", 
                "Racer", 
                "Red & Black", 
                "Special Edition"
            ),
            1, 
            50000
        ));
    }};

    @Override
    public void insertPost(Post post){
        listPosts.add(post);
        System.out.println("Post añadido");
    }

    @Override
    public List<Post> getAll() {
        return listPosts;
    }

    

}
