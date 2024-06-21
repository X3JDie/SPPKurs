package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import org.zeroturnaround.zip.ZipUtil;
import ru.kata.spring.boot_security.demo.models.Document;
import ru.kata.spring.boot_security.demo.services.DocumentService;

import javax.validation.constraints.Null;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocuments(@RequestParam("files") MultipartFile[] files,
                                                  @RequestParam("title") String title,
                                                  @RequestParam("department") String department) {
        try {
            StringBuilder fileNames = new StringBuilder();

            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                String filePath = "uploads/" + originalFilename;
                Files.write(Paths.get(filePath), file.getBytes());

                fileNames.append(filePath).append(";");
            }

            Document document = new Document();
            document.setTitle(title);
            document.setDepartment(department);
            document.setFilePath(fileNames.toString());
            document.setStatus("Wait");
            document.setUploadDate(new Date());

            documentService.save(document);

            return ResponseEntity.ok("Documents uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Document upload failed.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Optional<Document> documentOptional = documentService.findById(id);
        if (documentOptional.isPresent()) {
            return ResponseEntity.ok(documentOptional.get());
        } else {
            System.err.println("Document not found with id: " + id);
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Optional<Document> documentOptional = documentService.findById(id);
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            String[] filePaths = document.getFilePath().split(";");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipUtil.pack(Paths.get("uploads").toFile(), byteArrayOutputStream, name -> {
                for (String filePath : filePaths) {
                    if (name.equals(Paths.get(filePath).getFileName().toString())) {
                        return filePath;
                    }
                }
                return null;
            });

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            String encodedFilename;
            encodedFilename = UriUtils.encode(document.getTitle() + ".zip", StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            System.err.println("Document not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}/downloadsales")
    public ResponseEntity<Resource> downloadDocumentSales(@PathVariable Long id) {
        Optional<Document> documentOptional = documentService.findById(id);
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            document.setStatus("Accepted");
            document.setAcceptedDate(new Date());

            documentService.save(document);
            String[] filePaths = document.getFilePath().split(";");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipUtil.pack(Paths.get("uploads").toFile(), byteArrayOutputStream, name -> {
                for (String filePath : filePaths) {
                    if (name.equals(Paths.get(filePath).getFileName().toString())) {
                        return filePath;
                    }
                }
                return null;
            });

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            String encodedFilename;
            encodedFilename = UriUtils.encode(document.getTitle() + ".zip", StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            System.err.println("Document not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        try {
            documentService.delete(id);
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting document with id: " + id + " - " + e.getMessage());
            return ResponseEntity.status(500).body("Document deletion failed.");
        }
    }
}
