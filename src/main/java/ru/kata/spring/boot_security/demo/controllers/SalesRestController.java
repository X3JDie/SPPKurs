package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Document;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.DocumentServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {

    private final UserService userService;
    private final DocumentServiceImpl documentServiceImpl;

    @Autowired
    public SalesRestController(UserService userService, DocumentServiceImpl documentServiceImpl) {
        this.userService = userService;
        this.documentServiceImpl = documentServiceImpl;
    }

    @GetMapping("")
    public ResponseEntity<User> salesInfo(Principal principal) {
        return new ResponseEntity<>(userService.findByUsername(principal.getName()).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        String department = "SALES";
        String status = "WAIT";
        List<Document> documents = documentServiceImpl.findByDepartmentAndStatus(department, status);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/documentsaccepted")
    public ResponseEntity<List<Document>> getDocumentsAccepted() {
        String department = "SALES";
        String status = "Accepted";
        List<Document> documents = documentServiceImpl.findByDepartmentAndStatus(department, status);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Optional<Document> documentOptional = documentServiceImpl.findById(id);
        if (documentOptional.isPresent()) {
            return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
