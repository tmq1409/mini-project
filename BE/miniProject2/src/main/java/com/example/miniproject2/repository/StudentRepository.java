package com.example.miniproject2.repository;

import com.example.miniproject2.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("""
    SELECT s FROM Student s 
    WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
       OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
       OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) 
       OR s.phoneNumber LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Student> search(@Param("keyword") String keyword, Pageable pageable);
}
