package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kata.spring.boot_security.demo.models.Document;
import ru.kata.spring.boot_security.demo.services.DocumentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("description") String description) {
        try {
            // Сохранение информации о документе в базу данных
            Document document = new Document();
            document.setTitle(title);
            document.setDescription(description);
            document.setStatus("Draft");
            // Сохранение файла в виде массива байтов в базе данных
            document.setContent(file.getBytes());
            documentService.save(document);

            return ResponseEntity.ok("Document uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Document upload failed.");
        }
    }


    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable int id) {
        Optional<Document> documentOptional = documentService.findById(id);
        return documentOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable int id) {
        Optional<Document> documentOptional = documentService.findById(id);
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            try {
                Path filePath = Paths.get(document.getFilePath());
                byte[] data = Files.readAllBytes(filePath);

                ByteArrayResource resource = new ByteArrayResource(data);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitle() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            } catch (IOException e) {
                return ResponseEntity.status(500).body(null);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable int id) {
        try {
            documentService.delete(id);
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Document deletion failed.");
        }
    }
}
