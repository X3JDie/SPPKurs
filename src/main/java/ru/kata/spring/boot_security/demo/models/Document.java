package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String department; // Поле для хранения департамента
    private Date uploadDate;   // Поле для хранения даты загрузки
    private String status;
    private LocalDateTime receivedDate;
    private Date acceptedDate;
    @Column(length = 5000)
    private String filePath;


    public Document(String title, String department, String resolution, Date uploadDate, String status, Date acceptedDate, String filePath) {
        this.title = title;
        this.department = department;
        this.uploadDate = uploadDate;
        this.status = status;
        this.acceptedDate = acceptedDate;
        this.filePath = filePath;
    }

    public Document() {

    }

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
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

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }
}
