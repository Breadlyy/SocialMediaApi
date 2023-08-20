package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.entity.ImageEntity;
import com.example.socialmediaapi.repo.ImageRepository;
import com.example.socialmediaapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ImageController {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload_form"; // создайте шаблон Thymeleaf для загрузки файла
    }

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            ImageEntity image = new ImageEntity();
            image.setName(file.getOriginalFilename());
            image.setData(file.getBytes());
            imageRepository.save(image);
        }
        return "redirect:/upload";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<ImageEntity> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            ImageEntity image = imageOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // или другой тип в зависимости от формата
                    .body(image.getData());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/images")
    public String showAllImages(Model model) {
        List<ImageEntity> images = imageRepository.findAll();
        model.addAttribute("images", images);
        return "image_list"; // создайте Thymeleaf шаблон для отображения списка картинок
    }
}

