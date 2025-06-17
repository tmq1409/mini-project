package com.example.miniproject2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "Id", length = 100)
    private String id;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "PhoneNumber", nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Avatar", nullable = true, length = 200)
    private String avatar;

}