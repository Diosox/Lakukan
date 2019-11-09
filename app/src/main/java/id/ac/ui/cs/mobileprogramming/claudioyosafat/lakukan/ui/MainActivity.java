package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.R;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.databinding.ActivityMainBinding;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.fragment.TodoListFragment;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.viewmodel.TodoViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TodoListFragment todoListFragment = new TodoListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.list_holder, todoListFragment);
            fragmentTransaction.commit();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            TodoListFragment todoListFragment = new TodoListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.list_holder, todoListFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_todos:
                todoViewModel.deleteAllTodos();
                Toast.makeText(this, getResources().getString(R.string.todo_all_delete),
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
