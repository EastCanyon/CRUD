package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Controller
@RequestMapping("/posts")
public class PageController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String showPostList(@RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize,
                               Model model) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Post> postsPage = postService.getAllPosts(pageable);
        model.addAttribute("postsPage", postsPage);
        return "index";
    }

    @GetMapping(params = {"page"})
    public String showPostsByPage(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10; // 페이지당 표시될 게시글 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Post> postsPage = postService.getAllPosts(pageable);
        model.addAttribute("postsPage", postsPage);
        return "index"; // 또는 적절한 뷰 이름으로 변경
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "create_post_form";
    }

    @GetMapping("/create")
    public String showCreateFormGet(Model model) {
        model.addAttribute("post", new Post());
        return "create_post_form";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/posts/";
    }

    // 게시글 조회 페이지
    @GetMapping("/post/{postId}")
    public String showPostDetails(@PathVariable Long postId, Model model) {
        Optional<Post> postOptional = postService.getPostById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            // 조회수 증가
            post.setViewCount(post.getViewCount() + 1);
            postService.updatePost(postId, post); // 업데이트된 게시글 정보 저장
            // 게시물의 작성일을 형식화
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = post.getCreatedAt().format(formatter);
            // 형식화된 작성일을 모델에 추가
            model.addAttribute("formattedDateTime", formattedDateTime);
            model.addAttribute("post", post);
            return "post_details";
        } else {
            return "error"; // 예를 들어 error.html을 만들어서 해당 페이지를 보여줄 수 있습니다.
        }
    }
    @GetMapping("/edit/{postId}")
    public String showEditForm (@PathVariable Long postId, Model model){
        Optional<Post> postOptional = postService.getPostById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute("post", post);
            return "edit_post_form";
        } else {
            return "error"; // 예를 들어 error.html을 만들어서 해당 페이지를 보여줄 수 있습니다.
        }
    }
        // 게시글 수정 기능
    @PostMapping("/edit/{postId}")
    public String editPost (@PathVariable Long postId, @ModelAttribute Post editedPost){
            // postId와 editedPost를 서비스로 전달하여 엔티티를 수정
            postService.updatePost(postId, editedPost);
            return "redirect:/posts/post/{postId}";
        }

        // 게시글 삭제 기능
    @GetMapping("/delete/{postId}")
    public String deletePost (@PathVariable Long postId){
            postService.deletePost(postId);
            return "redirect:/posts/";
        }

//    @GetMapping("/posts")
//    public String showPosts(@RequestParam(defaultValue = "1") int pageNo, Model model) {
//        // 페이지당 표시될 게시글 수
//        int pageSize = 10;
//        // 요청된 페이지 번호(0부터 시작)
//        int pageNumber = pageNo - 1;
//        if (pageNumber < 0) {
//            pageNumber = 0;
//        }
//        // Pageable 객체 생성
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        // 페이징된 게시글 가져오기
//        Page<Post> postsPage = postService.getAllPosts(pageable);
//        // 페이지 정보와 게시글 데이터를 모델에 추가
//        model.addAttribute("postsPage", postsPage);
//        return "posts";
//    }

}