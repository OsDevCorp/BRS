package com.misis.brs.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Основной класс базы данных, содержит перечень всех Dao
 * экземпляр этого класса неоюходим для подключения к бд
 * entities - список всех таблиц входящих в бд
 */
@Database(entities = {Mark.class, Hometask.class, News.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /*
        методы для получения интерфейчов для рабыоты с бд
     */
    public abstract MarkDao markDao();
    public abstract HometaskDao hometaskDao();
    public abstract NewsDao newsDao();
}
