package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DocumentService {
    List<Document> findAll();

    Document save(Document document);

    Optional<Document> findById(int id);

    void delete(int id);
}
