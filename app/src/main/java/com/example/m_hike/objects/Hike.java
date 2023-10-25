package com.example.m_hike.objects;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hikes")
public class Hike {
    @PrimaryKey(autoGenerate = true)
    public int hid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "location")
    public String location;
    @ColumnInfo(name = "year")
    public int year;
    @ColumnInfo(name = "month")
    public int month;
    @ColumnInfo(name = "day")
    public int day;
    @ColumnInfo(name = "parkingAvailable")
    public Boolean parkingIsAvailable;
    @ColumnInfo(name = "length")
    public int lengthOfHeightInMeters;
    @ColumnInfo(name = "difficulty")
    public String difficulty;
    @ColumnInfo(name = "description")
    public String desc;
    @ColumnInfo(name = "image")
    public Bitmap image;
    @ColumnInfo(name = "elevation")
    public int elevation;
    public Hike(){

    }
    public Hike(String name, String location, int year, int month, int day, Boolean parkingIsAvailable, int lengthOfHeightInMeters, String difficulty, String desc, Bitmap image, int elevation) {
        this.name = name;
        this.location = location;
        this.year = year;
        this.month = month;
        this.day = day;
        this.parkingIsAvailable = parkingIsAvailable;
        this.lengthOfHeightInMeters = lengthOfHeightInMeters;
        this.difficulty = difficulty;
        this.desc = desc;
        this.image = image;
        this.elevation = elevation;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Boolean getParkingIsAvailable() {
        return parkingIsAvailable;
    }

    public void setParkingIsAvailable(Boolean parkingIsAvailable) {
        this.parkingIsAvailable = parkingIsAvailable;
    }

    public int getLengthOfHeightInMeters() {
        return lengthOfHeightInMeters;
    }

    public void setLengthOfHeightInMeters(int lengthOfHeightInMeters) {
        this.lengthOfHeightInMeters = lengthOfHeightInMeters;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}