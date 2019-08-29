package com.misis.brs.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * список всех возможных запросов к DB для объекта News
 */
@Dao
public interface NewsDao {

    // при одинаковых текстах новостей обновляем всё остальное
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    /**
     * удаляет новости которые были созданы раньше dateNews даты
     * @param dateNews Время до которого все новости удаляются
     */
    @Query("DELETE FROM news WHERE dateNews < :dateNews")
    void deleteByDateNews(long dateNews);

    @Query("SELECT * FROM NEWS")
    News[] selectAll();
}
