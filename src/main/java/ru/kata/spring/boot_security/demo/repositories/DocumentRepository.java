package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Document;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    void deleteById(Long id);

    Optional<Document> findById(Long id);

    List<Document> findByDepartment(String department);

    List<Document> findByStatus(String status);
    List<Document> findByDepartmentAndStatus(String departamenrt,String status);

}
