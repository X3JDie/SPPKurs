package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.Document;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.DocumentServiceImpl;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/secretary")
public class SecretaryRestController {

    private final UserService userService;

    private final DocumentServiceImpl documentServiceImpl;

    private final RoleService roleService;

    @Autowired
    public SecretaryRestController(UserService userService, DocumentServiceImpl documentServiceImpl, RoleService roleService) {
        this.userService = userService;
        this.documentServiceImpl = documentServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping("")
    public ResponseEntity<User> secretaryInfo(Principal principal) {
        return new ResponseEntity<>(userService.findByUsername(principal.getName()).orElse(null), HttpStatus.OK);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        String department = "SALES";
        List<Document> documents = documentServiceImpl.findByDepartment(department);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/documentssecretary")
    public ResponseEntity<List<Document>> getDocumentsSecretary() {
        String department = "SECRETARY";
        List<Document> documents = documentServiceImpl.findByDepartment(department);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/documentssales")
    public ResponseEntity<List<Document>> getDocumentsSales() {
        String department = "SALES";
        List<Document> documents = documentServiceImpl.findByDepartment(department);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/documentsfinance")
    public ResponseEntity<List<Document>> getDocumentsfinance() {
        String department = "FINANCE";
        List<Document> documents = documentServiceImpl.findByDepartment(department);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

}

