package com.fastcampus.simplesns.repository;

import com.fastcampus.simplesns.model.entity.CommentEntity;
import com.fastcampus.simplesns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);
}
