package com.example.myapplication;

public class Call_User_Model {
    private String Name;
    private String id;
    private String profileImageUrl;
    private String Email;
    private String Available;
    private String search;
    private String Phone;
    private String Reg;
    private String Speciality;
    private String Dept;
    private String type;



    private String Status;

    public Call_User_Model() {
    }

    public Call_User_Model(String type, String name, String userUid, String imageURL, String email, String available, String search, String phone, String reg, String Speciality, String dept, String Status) {
        this.Name = name;
        this.id = userUid;
        this.profileImageUrl = imageURL;
        this.Email = email;
        this.Available = available;
        this.search = search;
        this.Phone = phone;
        this.Reg = reg;
        this.Speciality = Speciality;
        this.Dept = dept;
        this.Status = Status;
        this.type = type;

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getReg() {
        return Reg;
    }

    public void setReg(String reg) {
        Reg = reg;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }





}
