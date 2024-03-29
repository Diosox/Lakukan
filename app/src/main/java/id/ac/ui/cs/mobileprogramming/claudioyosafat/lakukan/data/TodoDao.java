package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    long insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("DELETE FROM todo_table")
    void deleteAllTodos();

    @Query("SELECT * FROM todo_table ORDER BY isPriority DESC")
    LiveData<List<Todo>> getAllTodos();
}
