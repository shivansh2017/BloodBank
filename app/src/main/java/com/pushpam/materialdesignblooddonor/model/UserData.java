package com.pushpam.materialdesignblooddonor.model;

import java.util.Calendar;


public class UserData {
    private String name;
    private String bloodGroup;
    private String city;
    private String phone;
    private String ageUser;
    private String emailUser;

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = "Email : "+emailUser;
    }

    public String getAge() {
        return ageUser;
    }

    public void setAge(String age) {
        //String year=age;
        //year=year.substring(0, 4);
        final Calendar c = Calendar.getInstance();
        int yearCurrent = c.get(Calendar.YEAR);
        try {
            int yearPrev = (int) Integer.parseInt(age.substring(0, 4));
            int ageYear=yearCurrent-yearPrev;
            ageUser="Age : "+ Integer.toString(ageYear);
        }
        catch(NumberFormatException numberEx) {
            System.out.print(numberEx);
        }


    }




    public UserData() {

    }

    public UserData(String name, String bloodGroup, String city, String phone) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
