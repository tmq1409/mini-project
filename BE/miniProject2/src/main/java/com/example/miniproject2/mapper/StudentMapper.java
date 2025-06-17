package com.example.miniproject2.mapper;

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

}
