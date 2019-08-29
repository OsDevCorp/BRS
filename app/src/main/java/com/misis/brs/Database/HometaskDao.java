package com.misis.brs.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * список всех возможных запросов к DB для объекта Hometask
 */
@Dao
public interface HometaskDao {
    /**
     * добавляет запись о дз в бд
     * !!!!!!!
     * при уже существующей записи метод вернёт -1
     * @param hometask домашнее задание
     * @return возврашает id добавленного элемента
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Hometask hometask);

    /**
     * обновление дз
     * @param hometask домашнее задание
     * @return количество обновлённых записей
     */
    @Update
    int update(Hometask hometask);

    //задел на будущее
    /**
        возвращает уже завершившиеся задания функция для фильтра заданий
        @param deadline время для которого мы хотим вернуть все прошедшие записи
        @return список домашних заданий
     */
    @Query("SELECT * FROM hometask WHERE deadline < :deadline")
    Hometask[] selectOverdue(long deadline);

    /**
     * возвращает предстоящие несделанные задания
     * @param curTime время в секундах относительно которого счмтаем
     * @return список домашних заданий
     */
    @Query("SELECT * FROM hometask WHERE deadline > :curTime AND checkDone == 0")
    Hometask[] selectUpcoming(long curTime);

    @Query("SELECT * FROM hometask WHERE semester = :semester")
    Hometask[] selectForSemester(int semester);

    @Query("DELETE FROM hometask WHERE deadline = :deadline")
    void deleteByDeadline(long deadline);

    @Query("SELECT * FROM hometask WHERE deadline = :deadline")
    Hometask selectHometaskByDate(long deadline);

}
