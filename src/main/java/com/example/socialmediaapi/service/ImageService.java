package com.example.socialmediaapi.service;

import com.example.socialmediaapi.entity.ImageEntity;
import com.example.socialmediaapi.repo.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public void saveImage(String name, byte[] data) {
        ImageEntity image = new ImageEntity();
        image.setName(name);
        image.setData(data);
        imageRepository.save(image);
    }

    public byte[] getImageDataById(Long id) {
        Optional<ImageEntity> imageOptional = imageRepository.findById(id);
        return imageOptional.map(ImageEntity::getData).orElse(null);
    }
}
