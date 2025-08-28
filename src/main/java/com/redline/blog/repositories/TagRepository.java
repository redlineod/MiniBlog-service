package com.redline.blog.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.redline.blog.domain.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Query("SELECT t from Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllWithPostsCount();

    List<Tag> findByNameIn(Set<String> names);
}
