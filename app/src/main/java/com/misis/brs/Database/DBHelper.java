package com.misis.brs.Database;

import androidx.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * класс предназначен для обращения к DB
 * класс реализован с использованием паттерна одиночка
 * для начала работы надо вызвать метод getInstance в MainActivity
 */
public class DBHelper {
    private static DBHelper instance;
    private AppDatabase db;
    private static String dbName;

    /**
     * реализует паттерн одиночка
     * @param context контекст приложения
     * @param dbName фамилия и имя авторизованного пользователя в формате конкатенации
     */
    private DBHelper(Context context, String dbName){
        DBHelper.dbName = dbName;
        db = Room.databaseBuilder(context,AppDatabase.class, dbName).build();
    }

    /**
     * метод для получения сущности класса.
     * Необходимо вызвыать в методе Create активности и при смене авторизованного пользователя
     * для переключения баз данных в зависимости от авторизованного в текущий момент пользователя
     * @param context контекст текущей активности
     * @param dbName название базы данных к которой подключаемся.
     * Для каждого авторизованного пользователя название бд составляется в виде конкатенации Фамилии и имени
     * @return возвращает сущность, через которую производится работа с запросами
     * */
    public static DBHelper getInstance(Context context, String dbName){
        if(instance == null || !dbName.equals(DBHelper.dbName)){ //обновляем каждый раз, как меняем пользователя или создаём нового
            instance = new DBHelper(context, dbName);
        }
        return instance;
    }

    /**
     * Этот метод должен вызываться только после инициализации с помощью DBHelper.getInstance(...)
     * данный метод запускается из UI потока приложения и выполняет doInBackground в отдельном потоке.
     * Выгружает результат в UI поток и закрывает вспомогательный поток.
     * <тип входного параметра, ... , тип выходного параметра>
     * Параметры должны быть классами (обвёртками элементарных классов)
     * @param mark оценка, которую добавляем в бд
     */
    public static void insertMark(final Mark mark){
        AsyncTask<Mark, Void, Void> task = new AsyncTask<Mark, Void, Void>() {
            @Override
            protected Void doInBackground(Mark... marks) {
                instance.db.markDao().insert(mark);
                return null;
            }
        };
        task.execute(mark); // выполнение запроса
    }

    public static void deleteMarkById(final long id){
        AsyncTask<Long, Void, Void> task = new AsyncTask<Long, Void, Void>() {
            @Override
            protected Void doInBackground(Long... longs) {
                instance.db.markDao().deleteById(id);
                return null;
            }
        };
        task.execute(id);
    }

    /**
     * @param semester порядковый номер семестра начиная с 1
     */
    public static Mark[] selectMarksForSemester(final int semester){
        AsyncTask<Integer, Void, Mark[]> task = new AsyncTask<Integer, Void, Mark[]>() {
            @Override
            protected Mark[] doInBackground(Integer... integers) {
                return instance.db.markDao().selectForSemester(semester);
            }
        };
        task.execute(semester);
        try{
            return task.get(); // возвращает массив результата запроса
        } catch (InterruptedException e) {
            Log.e("selectMarksForSemester", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectMarksForSemester", e.toString());
        } return null;
    }

    public static Mark[] selectMarksForSemesterAndType(final int semester, final MarkType markType){
        AsyncTask<Object, Void, Mark[]> task = new AsyncTask<Object, Void, Mark[]>() {
            @Override
            protected Mark[] doInBackground(Object... objects) {
                return instance.db.markDao().selectForSemesterAndType((Integer)objects[0], (MarkType)objects[1]); // так как AsyncTask на вход подаёт varArgs,
                                                                                                // то со входными параметрами надо работать как с массивом
            }
        };
        task.execute(semester,markType);
        try{
            return task.get();
        } catch (InterruptedException e) {
            Log.e("selectMarksForSem&Type", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectMarksForSem&Type", e.toString());
        }   return null;
    }

    public static Long insertHometask(final Hometask hometask){
        AsyncTask<Hometask, Void, Long> task = new AsyncTask<Hometask, Void, Long>() {
            @Override
            protected Long doInBackground(Hometask... hometasks) {
                return instance.db.hometaskDao().insert(hometask);
            }
        };
        task.execute(hometask);
        try {
            return task.get();
        } catch (ExecutionException e) {
            Log.e("insertHometask", e.toString());
        } catch (InterruptedException e) {
            Log.e("insertHometask", e.toString());
        }
        return null;
    }

    public static Integer updateHometask (final Hometask hometask){
        AsyncTask<Hometask, Void, Integer> task = new AsyncTask<Hometask, Void, Integer>() {
            @Override
            protected Integer doInBackground(Hometask... hometasks) {
                return instance.db.hometaskDao().update(hometask);
            }
        };
        task.execute(hometask);
        try{
           return task.get();
        } catch (InterruptedException e) {
            Log.e("selectTaskByDeadline", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectTaskByDeadline", e.toString());
        }return null;
    }

    public static Hometask[] selectOverdueHometask(final Long deadline){
        AsyncTask<Long, Void, Hometask[]> task = new AsyncTask<Long, Void, Hometask[]>() {
            @Override
            protected Hometask[] doInBackground(Long... longs) {
                return instance.db.hometaskDao().selectOverdue(deadline);
            }
        };
        task.execute(deadline);
        try{
            return task.get();
        } catch (InterruptedException e) {
            Log.e("selectOverdueHometask", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectOverdueHometask", e.toString());
        }return null;
    }

    public static Hometask[] selectUpcoming(final Long curTime){
        AsyncTask<Long, Void, Hometask[]> task = new AsyncTask<Long, Void, Hometask[]>() {
            @Override
            protected Hometask[] doInBackground(Long... longs) {
                return instance.db.hometaskDao().selectUpcoming(curTime);
            }
        };
        task.execute(curTime);
        try{
            return task.get();
        } catch (InterruptedException e) {
            Log.e("selectOverdueHometask", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectOverdueHometask", e.toString());
        }return null;
    }

    public static Hometask[] selectHometaskForSemester(final int semester){
        AsyncTask<Integer, Void, Hometask[]> task = new AsyncTask<Integer, Void, Hometask[]>() {
            @Override
            protected Hometask[] doInBackground(Integer... integers) {
                return instance.db.hometaskDao().selectForSemester(semester);
            }
        };
        task.execute(semester);
        try{
            return task.get();
        } catch (InterruptedException e) {
            Log.e("selectTaskForSemester", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectTaskForSemester", e.toString());
        }return null;
    }

    public static Hometask selectHometaskByDate(final long deadline){
        AsyncTask<Long, Void, Hometask> task = new AsyncTask<Long, Void, Hometask>() {
            @Override
            protected Hometask doInBackground(Long... longs) {
                return instance.db.hometaskDao().selectHometaskByDate(deadline);
            }
        };
        task.execute(deadline);
        try{
            return task.get();
        } catch (InterruptedException e) {
            Log.e("selectTaskForSemester", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectTaskForSemester", e.toString());
        }return null;
    }

    public static void deleteHometaskByDeadline(final long deadline){
        AsyncTask<Long, Void, Void> task = new AsyncTask<Long, Void, Void>() {
            @Override
            protected Void doInBackground(Long... longs) {
                instance.db.hometaskDao().deleteByDeadline(deadline);
                return null;
            }
        };
        task.execute(deadline);
    }

    public static void insertNews(final News news1){
        AsyncTask<News, Void, Void> task = new AsyncTask<News, Void, Void>() {
            @Override
            protected Void doInBackground(News... news) {
                instance.db.newsDao().insert(news1);
                return null;
            }
        };
        task.execute(news1);
    }

    public static void deleteNewsByDateNews(final long dateNews){
        AsyncTask<Long, Void, Void> task = new AsyncTask<Long, Void, Void>() {
            @Override
            protected Void doInBackground(Long... longs) {
                instance.db.newsDao().deleteByDateNews(dateNews);
                return null;
            }
        };
        task.execute(dateNews);
    }

    public static News[] selectAllNews(){
        AsyncTask<Void, Void, News[]> task = new AsyncTask<Void, Void, News[]>() {
            @Override
            protected News[] doInBackground(Void... voids) {
                return instance.db.newsDao().selectAll();
            }
        };
        task.execute();
        try{
            return task.get();
        } catch (InterruptedException e) {
            Log.e("selectAllNews", e.toString());
        } catch (ExecutionException e) {
            Log.e("selectAllNews", e.toString());
        }return null;
    }

    /**
     * Функция проверки возможности добавления оценки для сходимости суммы
     * @param mark добавляемая оценка
     * @return 0 - можно добавить, 1 - лимит количества, 2 - лимит суммы баллов
     */
    public static int isAbleToAdd(Mark mark){
        Mark[] ans = selectMarksForSemesterAndType(mark.getSemester(),mark.getMarkType());
        if (ans == null) {
            return 0;
        }
        switch (mark.getMarkType()){
            case CLASS_PARTICIPATION_PART_1:
            case CLASS_PARTICIPATION_PART_2:
                int sum = 0;
                for (int i = 0; i < ans.length; i++)
                    sum += ans[i].getMark();
                if (sum + mark.getMark() > 20)
                    return 2;
                else break;
            case ONLINE_PART_1:
            case ONLINE_PART_2:
            case MIDTERM:
            case FINAL_TEST:
                if (ans.length != 0)
                    return 1;
                else break;
            case PROJECT_PART_1:
            case PROJECT_PART_2:
                if (ans.length >= 2)
                    return 1;
                else break;
        }
        return 0;
    }
}
