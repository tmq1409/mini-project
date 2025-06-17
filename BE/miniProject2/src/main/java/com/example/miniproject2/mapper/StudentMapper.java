package com.example.miniproject2.mapper;

import com.example.miniproject2.dto.request.StudentRequest;
import com.example.miniproject2.dto.response.StudentResponse;
import com.example.miniproject2.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    @Value("${app.host}")
    private String host;

    public StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .phoneNumber(student.getPhoneNumber())
                .email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth())
                .avatar(host + student.getAvatar())
                .build();
    }

    public Student toEntity(Student student, StudentRequest request, String avatarUrl) {
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());

        if (avatarUrl != null) {
            student.setAvatar(avatarUrl);
        }

        return student;
    }

}
