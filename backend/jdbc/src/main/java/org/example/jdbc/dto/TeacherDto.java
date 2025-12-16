package org.example.jdbc.dto;

public class TeacherDto {
    // Teacher信息
    private String teacherId;
    private String name;
    private String title;
    private String office;
    private String phone;
    private String duty;
    private String majorCode;
    
    // UserAccount信息
    private String userId;
    private String password;
    private Integer status;
    
    // Getters and Setters
    public String getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getOffice() {
        return office;
    }
    public void setOffice(String office) {
        this.office = office;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDuty() {
        return duty;
    }
    public void setDuty(String duty) {
        this.duty = duty;
    }
    public String getMajorCode() {
        return majorCode;
    }
    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}