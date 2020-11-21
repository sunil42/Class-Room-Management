package com.example.classroommanagement;

public class teachermodel 
{
    private String Name;
    private String Designation;
    private String Phone;
    private String Email;

    public teachermodel() { }

    private teachermodel(String teachername, String teacherdesig, String teacherphone, String teacheremail)
    {
        this.Name = teachername;
        this.Designation = teacherdesig;
        this.Phone = teacherphone;
        this.Email = teacheremail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
