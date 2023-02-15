package com.supportivehands.salonmanagementapp.registeration.customers;

public class ModelRegistrCustmer {
    String propfilepic;
    String name;
    String mobie;
    String email;
    String uid;
    String pass;
    public ModelRegistrCustmer(String propfilepic, String name, String mobie, String email, String uid,String pass) {
        this.propfilepic = propfilepic;
        this.name = name;
        this.mobie = mobie;
        this.email = email;
        this.uid = uid;
        this.pass=pass;
    }

    public String getPropfilepic() {
        return propfilepic;
    }

    public void setPropfilepic(String propfilepic) {
        this.propfilepic = propfilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobie() {
        return mobie;
    }

    public void setMobie(String mobie) {
        this.mobie = mobie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
