package com.supportivehands.salonmanagementapp.model;

public class modelRegistr {

    private String lgo,shopName,ownerName,mobileNo,adress,city,mapAdress,lat,lng,email,pas,uid,userType,status;

    public modelRegistr(String lgo, String shopName, String ownerName, String mobileNo, String adress, String city, String mapAdress, String lat, String lng, String email, String pas,String uid,String usertype,String status) {
        this.lgo = lgo;
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.mobileNo = mobileNo;
        this.adress = adress;
        this.city = city;
        this.mapAdress = mapAdress;
        this.lat = lat;
        this.lng = lng;
        this.email = email;
        this.pas = pas;
        this.uid=uid;
        this.userType=usertype;
    }

    public modelRegistr() {
    }

    public String getLgo() {
        return lgo;
    }

    public void setLgo(String lgo) {
        this.lgo = lgo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMapAdress() {
        return mapAdress;
    }

    public void setMapAdress(String mapAdress) {
        this.mapAdress = mapAdress;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
