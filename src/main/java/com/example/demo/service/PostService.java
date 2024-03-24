package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void updatePost(Long postId, Post updatedPost) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitle(updatedPost.getTitle());
            post.setAuthor(updatedPost.getAuthor());
            post.setContent(updatedPost.getContent());
            // 여기서 필요에 따라 작성일 등을 업데이트할 수 있습니다.
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다: " + postId);
        }
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
