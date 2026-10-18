package ru.job4j.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String save(MultipartFile file);
}
