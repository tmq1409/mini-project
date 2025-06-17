package com.example.miniproject2.service;

import com.example.miniproject2.dto.request.StudentRequest;
import com.example.miniproject2.dto.response.StudentResponse;
import com.example.miniproject2.entity.Student;
import com.example.miniproject2.exception.ResourceNotFoundException;
import com.example.miniproject2.mapper.StudentMapper;
import com.example.miniproject2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final FileService fileService;

    public Page<StudentResponse> getAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll(pageable)
                    .map(studentMapper::toResponse);
        }
        return studentRepository.search(keyword.trim(), pageable)
                .map(studentMapper::toResponse);
    }

    public StudentResponse getById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return studentMapper.toResponse(student);
    }

    public StudentResponse create(StudentRequest request) throws IOException {
        if (studentRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }
        String avatarUrl = fileService.uploadFile(request.getAvatar());
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student = studentMapper.toEntity(student, request, avatarUrl);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    public StudentResponse update(String id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        String avatarUrl = null;
        MultipartFile newAvatar = request.getAvatar();
        if (newAvatar != null && !newAvatar.isEmpty()) {
            fileService.deleteFile(student.getAvatar());
            try { avatarUrl = fileService.uploadFile(newAvatar); }
            catch (IOException e) { throw new RuntimeException("Failed to upload new avatar", e); }
        }
        student = studentMapper.toEntity(student, request, avatarUrl);
        return studentMapper.toResponse(studentRepository.save(student));
    }


    public void delete(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (student.getAvatar() != null) {
            fileService.deleteFile(student.getAvatar());
        }
        studentRepository.deleteById(id);
    }
}
