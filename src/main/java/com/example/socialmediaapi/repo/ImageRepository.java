package com.example.socialmediaapi.repo;

import com.example.socialmediaapi.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
