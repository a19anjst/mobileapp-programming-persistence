package com.example.persistence;

public class Gods {
    private String name;
    private String location;
    private String gender;

    public Gods(){
        name="Saknar namn";
        location="Saknar plats";
        gender="Saknar kön";
    }
    public Gods(String inName, String inLocation, String inGender){
        name=inName;
        location=inLocation;
        gender=inGender;
    }
}
