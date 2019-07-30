package com.misis.brs.Database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
public class Mark {
    //автоинкремент
    @PrimaryKey(autoGenerate = true)
    private long id;
    //анатация указывает какой класс обрабатывает конвертацию
    @TypeConverters({Mark.class})
    private MarkType markType;
    private int semester;
    private int mark;
    private int maxMark;
    private String description;

    // методы  конвертации Enum в int и обратно для хранения в бд
    @TypeConverter
    public int fromMarkType(MarkType markType){
        return markType.ordinal();
    }

    @TypeConverter
    public MarkType toMarkType(int value){
        return MarkType.values()[value];
    }



    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(int maxMark) {
        this.maxMark = maxMark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
