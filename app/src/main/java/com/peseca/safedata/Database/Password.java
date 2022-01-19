package com.peseca.safedata.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Password {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "app_name")
    public String app_name;

    @ColumnInfo(name = "hashed_password")
    public String hashed_password;
}
