package com.misis.brs.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * класс описываюший сущность домашнего задания.
 * на основе этого класса строится одноимённая таблица
 * !!!!!!!!!!
 * get и set методы необходимы для того, чтобы room runtime мог работать с private полями в классе
 */
@Entity
public class Hometask {
    @PrimaryKey() // анотация первичного ключа для таблицы
    private final long deadline; // неизменяемый параметр для дз
    private String description;
    private Boolean checkDone;
    private int semester;
    private Boolean checkNotify;
    private long timeNotification;

    /**
     * конструктор необходим для объявления final переменной
     * @param deadline дата окончания домашней работы, до которой её надо выполненить
     */
    public Hometask(long deadline) {
        this.deadline = deadline;
    }

    public long getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCheckDone() {
        return checkDone;
    }

    public void setCheckDone(Boolean checkDone) {
        this.checkDone = checkDone;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Boolean getCheckNotify() {
        return checkNotify;
    }

    public void setCheckNotify(Boolean checkNotify) {
        this.checkNotify = checkNotify;
    }

    public long getTimeNotification() {
        return timeNotification;
    }

    public void setTimeNotification(long timeNotification) {
        this.timeNotification = timeNotification;
    }
}
