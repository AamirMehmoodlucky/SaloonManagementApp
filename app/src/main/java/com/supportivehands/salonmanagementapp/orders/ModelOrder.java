package com.supportivehands.salonmanagementapp.orders;

public class ModelOrder {
    String title,url,price,lat,lng,orderstatus,uidbuyer,uidseller,mobileSeller,mobileBuyer,strttime,endtime,pushkey;

    public ModelOrder(String title, String url, String price, String lat, String lng, String orderstatus, String uidbuyer, String uidseller,String mobileSeller,String mobileBuyer,String strttime,String endtime,String pushkey) {
        this.title = title;
        this.url = url;
        this.price = price;
        this.lat = lat;
        this.lng = lng;
        this.orderstatus = orderstatus;
        this.uidbuyer = uidbuyer;
        this.uidseller = uidseller;
        this.mobileBuyer=mobileBuyer;
        this.mobileSeller=mobileSeller;
        this.strttime=strttime;
        this.endtime=endtime;
        this.pushkey=pushkey;
    }

    public ModelOrder() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getUidbuyer() {
        return uidbuyer;
    }

    public void setUidbuyer(String uidbuyer) {
        this.uidbuyer = uidbuyer;
    }

    public String getUidseller() {
        return uidseller;
    }

    public void setUidseller(String uidseller) {
        this.uidseller = uidseller;
    }

    public String getMobileSeller() {
        return mobileSeller;
    }

    public void setMobileSeller(String mobileSeller) {
        this.mobileSeller = mobileSeller;
    }

    public String getMobileBuyer() {
        return mobileBuyer;
    }

    public void setMobileBuyer(String mobileBuyer) {
        this.mobileBuyer = mobileBuyer;
    }

    public String getStrttime() {
        return strttime;
    }

    public void setStrttime(String strttime) {
        this.strttime = strttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }
}
