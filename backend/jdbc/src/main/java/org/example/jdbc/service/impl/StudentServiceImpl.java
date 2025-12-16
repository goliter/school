package org.example.jdbc.service.impl;

import org.example.jdbc.dto.StudentDto;
import org.example.jdbc.entity.Student;
import org.example.jdbc.entity.UserAccount;
import org.example.jdbc.repository.StudentRepository;
import org.example.jdbc.repository.UserAccountRepository;
import org.example.jdbc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, UserAccountRepository userAccountRepository) {
        this.studentRepository = studentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public List<Student> getStudentsByMajor(String majorCode) {
        return studentRepository.findByMajorCode(majorCode);
    }

    @Override
    public boolean addStudent(StudentDto studentDto) {
        try {
            // 1. 创建UserAccount
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(studentDto.getUserId());
            userAccount.setPassword(studentDto.getPassword());
            userAccount.setRole("student");
            userAccount.setStatus(studentDto.getStatus() != null ? studentDto.getStatus() : 1);
            userAccount.setCreateTime(new Date());
            
            // 2. 保存UserAccount
            userAccount = userAccountRepository.save(userAccount);
            
            // 3. 创建Student
            Student student = new Student();
            student.setStudentId(studentDto.getStudentId());
            student.setName(studentDto.getName());
            student.setGender(studentDto.getGender());
            student.setBirthDate(studentDto.getBirthDate());
            student.setPhone(studentDto.getPhone());
            student.setMajorCode(studentDto.getMajorCode());
            
            // 4. 关联UserAccount到Student
            student.setUserAccount(userAccount);
            
            // 5. 保存Student
            studentRepository.save(student);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStudent(StudentDto studentDto) {
        if (studentRepository.existsById(studentDto.getStudentId())) {
            try {
                // 1. 获取Student
                Student student = studentRepository.findById(studentDto.getStudentId()).orElse(null);
                if (student == null) {
                    return false;
                }
                
                // 2. 获取或创建UserAccount
                UserAccount userAccount = student.getUserAccount();
                if (userAccount == null) {
                    userAccount = new UserAccount();
                    userAccount.setUserId(studentDto.getUserId());
                    userAccount.setRole("student");
                }
                
                // 3. 更新UserAccount信息
                userAccount.setPassword(studentDto.getPassword());
                userAccount.setStatus(studentDto.getStatus() != null ? studentDto.getStatus() : 1);
                
                // 4. 保存UserAccount
                userAccount = userAccountRepository.save(userAccount);
                
                // 5. 更新Student信息
                student.setName(studentDto.getName());
                student.setGender(studentDto.getGender());
                student.setBirthDate(studentDto.getBirthDate());
                student.setPhone(studentDto.getPhone());
                student.setMajorCode(studentDto.getMajorCode());
                student.setUserAccount(userAccount);
                
                // 6. 保存Student
                studentRepository.save(student);
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
            return true;
        }
        return false;
    }
}