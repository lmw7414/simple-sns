package com.fastcampus.simplesns.repository;

import com.fastcampus.simplesns.model.entity.CommentEntity;
import com.fastcampus.simplesns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    //JPA에서 제공하는 delete의 경우 사용하지 않는 것이 좋다. 영속성에 가져와서 비교 후 삭제하기 때문에 비효율적이다. 따라서 쿼리를 통해 DB에서 바로 삭제가 일어나도록 하자
    @Transactional
    @Modifying
    @Query("UPDATE CommentEntity entity SET deleted_at = NOW() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}
