package com.fastcampus.simplesns.repository;

import com.fastcampus.simplesns.model.entity.AlarmEntity;
import com.fastcampus.simplesns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {
    Page<AlarmEntity> findAllByUserId(Integer userId, Pageable pageable);
}
