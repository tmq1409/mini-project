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

        String avatarUrl = uploadAvatar(request.getAvatar());

        Student student = Student.builder()
                .id(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .avatar(avatarUrl)
                .build();

        return studentMapper.toResponse(studentRepository.save(student));
    }

    public StudentResponse update(String id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        MultipartFile newAvatar = request.getAvatar();
        if (newAvatar != null && !newAvatar.isEmpty()) {
            if (student.getAvatar() != null) {
                try {
                    Path oldPath = Paths.get(System.getProperty("user.dir"), "uploads").resolve(
                            Paths.get(student.getAvatar()).getFileName().toString()
                    );
                    Files.deleteIfExists(oldPath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete old avatar", e);
                }
            }

            try {
                String newAvatarUrl = uploadAvatar(newAvatar);
                student.setAvatar(newAvatarUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload new avatar", e);
            }
        }

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            student.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            student.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            student.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            student.setEmail(request.getEmail());
        }
        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }

        return studentMapper.toResponse(studentRepository.save(student));
    }

    private String uploadAvatar(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            System.out.println("File is null or empty");
            return null;
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename().trim();
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        Files.createDirectories(uploadDir);
        Path filePath = uploadDir.resolve(fileName);

        try {
            file.transferTo(filePath.toFile());
            System.out.println("File uploaded to: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Upload failed: " + e.getMessage());
        }

        return "/uploads/" + fileName;
    }

    public void delete(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (student.getAvatar() != null) {
            try {
                String fileName = Paths.get(student.getAvatar()).getFileName().toString();
                Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        studentRepository.deleteById(id);
    }
}
