package com.elevenzon.sqlite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface RoomDao {



    @Insert
    void insert(NoteModel noteModel);

    @Update
    void update();

    @Delete
    void delete();
}
