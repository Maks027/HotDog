package com.hotdog.controllers;


import com.hotdog.entities.Posts;
import com.hotdog.entities.User;
import com.hotdog.repositories.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class PostsController {

    private final PostsRepo postsRepo;

    public PostsController(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping(path = "posts/all")
    public String allPosts(Map<String, Object> model){
        Iterable<Posts> allPosts = postsRepo.findAll();
        model.put("ps", allPosts);
        return "posts/all";
    }

    @GetMapping("/posts")
    public String postForm(){
        return "posts";
    }

    @PostMapping("/posts")
    public String add(@RequestParam String pstName,
                      @RequestParam String pstText,
                      @RequestParam("file") MultipartFile file,
                      @AuthenticationPrincipal User user,
                      Map<String, Object> model) throws IOException {
        Posts p = new Posts();

        if(file != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdirs();
            }

            String uuidFile = UUID.randomUUID().toString();

            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            p.setImgName(resultFileName);
            file.transferTo(new File(uploadPath + "/" + resultFileName));
        }

        p.setPostName(pstName);
        p.setPostText(pstText);
        p.setAuthor(user);

        postsRepo.save(p);

        return "redirect:/posts/all";
    }

}