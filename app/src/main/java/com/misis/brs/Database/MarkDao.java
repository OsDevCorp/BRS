package com.misis.brs.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

/**
 * список всех возможных запросов к DB для объекта Mark
 */
@Dao
public interface MarkDao {

    @Insert
    void insert(Mark mark);

    @Query("DELETE FROM mark WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * FROM mark WHERE semester = :semester")
    Mark[] selectForSemester(int semester);

    @Query("SELECT * FROM mark WHERE semester = :semester AND markType = :markType")
    Mark[] selectForSemesterAndType(int semester, @TypeConverters({Mark.class}) MarkType markType);
}
