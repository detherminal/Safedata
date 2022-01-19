package com.peseca.safedata.Database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PasswordDAO {

    @Query("SELECT * FROM password")
    List<Password> getAllPasswords();

    @Insert
    void insertUser(Password... passwords);

    @Query("DELETE FROM password WHERE uid = :userId")
    void deleteByUserId(long userId);
}
