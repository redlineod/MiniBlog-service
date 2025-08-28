package com.redline.blog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redline.blog.domain.PostStatus;
import com.redline.blog.domain.entities.Category;
import com.redline.blog.domain.entities.Post;
import com.redline.blog.domain.entities.Tag;
import com.redline.blog.domain.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);

    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);

    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);

    List<Post> findAllByStatus(PostStatus status);

    List<Post> findAllByStatusAndAuthor(PostStatus status, User user);
}
