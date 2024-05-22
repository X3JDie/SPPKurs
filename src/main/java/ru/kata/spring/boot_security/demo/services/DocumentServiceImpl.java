package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kata.spring.boot_security.demo.models.Document;
import ru.kata.spring.boot_security.demo.repositories.DocumentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService{

    private final DocumentRepository documentRepository;
    private final Path fileStorageLocation;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Transactional
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Transactional
    public Optional<Document> findById(int id) {
        return Optional.empty();
    }

    @Transactional
    public void delete(int id) {
        documentRepository.deleteById(id);
    }

    @Transactional
    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

}
