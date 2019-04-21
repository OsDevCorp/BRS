package com.misis.brs.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Основной класс базы данных, содержит перечень всех Dao
 * entities - список всех таблиц
 */
@Database(entities = {Mark.class, Hometask.class, News.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarkDao markDao();
    public abstract HometaskDao hometaskDao();
    public abstract NewsDao newsDao();
}
