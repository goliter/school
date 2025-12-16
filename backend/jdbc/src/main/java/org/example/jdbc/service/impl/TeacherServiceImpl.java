package org.example.jdbc.service.impl;

import org.example.jdbc.dto.TeacherDto;
import org.example.jdbc.entity.Teacher;
import org.example.jdbc.entity.UserAccount;
import org.example.jdbc.repository.TeacherRepository;
import org.example.jdbc.repository.UserAccountRepository;
import org.example.jdbc.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, UserAccountRepository userAccountRepository) {
        this.teacherRepository = teacherRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId).orElse(null);
    }

    @Override
    public List<Teacher> getTeachersByMajor(String majorCode) {
        return teacherRepository.findByMajorCode(majorCode);
    }

    @Override
    public boolean addTeacher(TeacherDto teacherDto) {
        try {
            // 1. 创建UserAccount
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(teacherDto.getUserId());
            userAccount.setPassword(teacherDto.getPassword());
            userAccount.setRole("teacher");
            userAccount.setStatus(teacherDto.getStatus() != null ? teacherDto.getStatus() : 1);
            userAccount.setCreateTime(new Date());
            
            // 2. 保存UserAccount
            userAccount = userAccountRepository.save(userAccount);
            
            // 3. 创建Teacher
            Teacher teacher = new Teacher();
            teacher.setTeacherId(teacherDto.getTeacherId());
            teacher.setName(teacherDto.getName());
            teacher.setTitle(teacherDto.getTitle());
            teacher.setOffice(teacherDto.getOffice());
            teacher.setPhone(teacherDto.getPhone());
            teacher.setDuty(teacherDto.getDuty());
            teacher.setMajorCode(teacherDto.getMajorCode());
            
            // 4. 关联UserAccount到Teacher
            teacher.setUserAccount(userAccount);
            
            // 5. 保存Teacher
            teacherRepository.save(teacher);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTeacher(TeacherDto teacherDto) {
        if (teacherRepository.existsById(teacherDto.getTeacherId())) {
            try {
                // 1. 获取Teacher
                Teacher teacher = teacherRepository.findById(teacherDto.getTeacherId()).orElse(null);
                if (teacher == null) {
                    return false;
                }
                
                // 2. 获取或创建UserAccount
                UserAccount userAccount = teacher.getUserAccount();
                if (userAccount == null) {
                    userAccount = new UserAccount();
                    userAccount.setUserId(teacherDto.getUserId());
                    userAccount.setRole("teacher");
                }
                
                // 3. 更新UserAccount信息
                userAccount.setPassword(teacherDto.getPassword());
                userAccount.setStatus(teacherDto.getStatus() != null ? teacherDto.getStatus() : 1);
                
                // 4. 保存UserAccount
                userAccount = userAccountRepository.save(userAccount);
                
                // 5. 更新Teacher信息
                teacher.setName(teacherDto.getName());
                teacher.setTitle(teacherDto.getTitle());
                teacher.setOffice(teacherDto.getOffice());
                teacher.setPhone(teacherDto.getPhone());
                teacher.setDuty(teacherDto.getDuty());
                teacher.setMajorCode(teacherDto.getMajorCode());
                teacher.setUserAccount(userAccount);
                
                // 6. 保存Teacher
                teacherRepository.save(teacher);
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(String teacherId) {
        if (teacherRepository.existsById(teacherId)) {
            teacherRepository.deleteById(teacherId);
            return true;
        }
        return false;
    }
}