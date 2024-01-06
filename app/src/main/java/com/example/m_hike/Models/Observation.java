package com.example.m_hike.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "observations")
public class Observation {
    @PrimaryKey(autoGenerate = true)
    public int oid;
    @ColumnInfo(name = "hid")
    public int hid;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "text")
    public String text;

    public Observation(int hid, String name, String date, String text) {
        this.hid = hid;
        this.name = name;
        this.date = date;
        this.text = text;
    }
}
