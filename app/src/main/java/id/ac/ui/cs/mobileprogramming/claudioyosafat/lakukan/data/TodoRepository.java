package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TodoRepository {

    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodos;

    public TodoRepository(Application application) {

        TodoDatabase todoDatabase = TodoDatabase.getInstance(application);
        todoDao = todoDatabase.todoDao();

        allTodos = todoDao.getAllTodos();

    }

    public long insert(Todo todo) throws ExecutionException, InterruptedException {
        Long id = new InsertTodoAsyncTask(todoDao).execute(todo).get();
        return id;
    }

    public void update(Todo todo) {
        new UpdateTodoAsyncTask(todoDao).execute(todo);
    }

    public void delete(Todo todo) {
        new DeleteTodoAsyncTask(todoDao).execute(todo);
    }

    public void deleteAllTodos() {
        new DeleteAllTodoAsyncTask(todoDao).execute();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    private static class InsertTodoAsyncTask extends AsyncTask<Todo, Void, Long> {

        private TodoDao todoDao;

        private InsertTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Long doInBackground(Todo... todos) {
            long id = todoDao.insert(todos[0]);
            return id;
        }
    }

    private static class UpdateTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private UpdateTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.update(todos[0]);
            return null;
        }
    }

    private static class DeleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private DeleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.delete(todos[0]);
            return null;
        }
    }

    private static class DeleteAllTodoAsyncTask extends AsyncTask<Void, Void, Void> {

        private TodoDao todoDao;

        private DeleteAllTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todoDao.deleteAllTodos();
            return null;
        }
    }

}
