package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;
    private String filePath;  // Добавлено для хранения пути к файлу

    public Document() {
    }

    public Document(String title, String description, String status, String filePath) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.filePath = filePath;
    }

    // Геттеры и сеттеры...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setContent(byte[] bytes) {
    }
}
