package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data.Todo;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data.TodoRepository;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository todoRepository;
    private LiveData<List<Todo>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        todoRepository = new TodoRepository(application);
        allTodos = todoRepository.getAllTodos();
    }

    public long insert(Todo todo) throws ExecutionException, InterruptedException {
        return todoRepository.insert(todo);

    }

    public void update(Todo todo) {
        todoRepository.update(todo);
    }

    public void delete(Todo todo) {
        todoRepository.delete(todo);
    }

    public void deleteAllTodos() {
        todoRepository.deleteAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return todoRepository.getAllTodos();
    }
}
