package com.elevenzon.sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface RoomDao {



    @Insert
    void insert(NoteModel noteModel);

    @Update
    void update();

    @Delete
    void delete();
}
