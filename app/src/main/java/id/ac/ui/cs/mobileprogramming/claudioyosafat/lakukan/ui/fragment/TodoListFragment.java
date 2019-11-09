package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.R;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.notification.AlarmReceiver;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data.Todo;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.databinding.TodoListFragmentBinding;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.AddEditTodoActivity;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.DescriptionActivity;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.OnFavClickListener;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.OnTodoClickListener;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.adapter.TodoAdapter;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.swipe.TodoSwipe;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.swipe.TodoSwipeControllerActions;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.viewmodel.TodoViewModel;

public class TodoListFragment extends Fragment implements OnTodoClickListener, OnFavClickListener {

    public static final int ADD_TODO_REQUEST = 1;
    public static final int EDIT_TODO_REQUEST = 2;

    private TodoListFragmentBinding binding;
    private TodoViewModel todoViewModel;
    private TodoAdapter todoAdapter;
    private TodoSwipe todoSwipe;

    private boolean dualPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.todo_list_fragment, container, false);
        final View todoView = binding.getRoot();

        FloatingActionButton buttonAddTodo = todoView.findViewById(R.id.button_add);
        buttonAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEditTodoActivity.class);
                startActivityForResult(intent, ADD_TODO_REQUEST);
            }
        });
        return todoView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View detailsFrame = getActivity().findViewById(R.id.details);

        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        todoAdapter = new TodoAdapter(getActivity(), this, this);
        binding.recyclerView.setAdapter(todoAdapter);

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                todoAdapter.setTodos(todos);
            }
        });

        todoSwipe = new TodoSwipe(new TodoSwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Todo todo = todoAdapter.getTodoAt(position);

                todoViewModel.delete(todoAdapter.getTodoAt(position));
                todoAdapter.notifyItemRemoved(position);
                todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                cancelAlarm(todo, todo.getId());
                Toast.makeText(getActivity(), getResources().getString(R.string.todo_delete), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLeftClicked(int position) {
                Todo todo = todoAdapter.getTodoAt(position);

                Intent intent = new Intent(getActivity(), AddEditTodoActivity.class);
                intent.putExtra(AddEditTodoActivity.EXTRA_ID, todo.getId());
                intent.putExtra(AddEditTodoActivity.EXTRA_TITLE, todo.getTitle());
                intent.putExtra(AddEditTodoActivity.EXTRA_LOCATION, todo.getLocation());
                intent.putExtra(AddEditTodoActivity.EXTRA_DESCRIPTION, todo.getDescription());
                intent.putExtra(AddEditTodoActivity.EXTRA_TIME, todo.getTime());
                intent.putExtra(AddEditTodoActivity.EXTRA_DATE, todo.getDate());
                startActivityForResult(intent, EDIT_TODO_REQUEST);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(todoSwipe);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        binding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                todoSwipe.onDraw(c);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TODO_REQUEST && resultCode == getActivity().RESULT_OK) {
            long todoId;

            String title = data.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTodoActivity.EXTRA_DESCRIPTION);
            String location = data.getStringExtra(AddEditTodoActivity.EXTRA_LOCATION);
            long time = data.getLongExtra(AddEditTodoActivity.EXTRA_TIME, 0L);
            long date = data.getLongExtra(AddEditTodoActivity.EXTRA_DATE, 0L);
            long reminder = data.getLongExtra(AddEditTodoActivity.EXTRA_REMINDER, 0L);

            Todo todo = new Todo(title, description, false, false, time,
                    date, location, reminder);
            try {
                todoId = todoViewModel.insert(todo);
                startAlarm(time, date, todo, todoId);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(getActivity(), getResources().getString(R.string.todo_added)
                    , Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TODO_REQUEST && resultCode == getActivity().RESULT_OK) {

            int id = data.getIntExtra(AddEditTodoActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getActivity(), getResources().getString(R.string.todo_update_failed),
                        Toast.LENGTH_SHORT);
            }

            String title = data.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTodoActivity.EXTRA_DESCRIPTION);
            String location = data.getStringExtra(AddEditTodoActivity.EXTRA_LOCATION);
            long time = data.getLongExtra(AddEditTodoActivity.EXTRA_TIME, 0L);
            long date = data.getLongExtra(AddEditTodoActivity.EXTRA_DATE, 0L);
            long reminder = data.getLongExtra(AddEditTodoActivity.EXTRA_REMINDER, 0L);

            Todo todo = new Todo(title, description, false, false, time, date
                    , location, reminder);
            todo.setId(id);

            cancelAlarm(todo, id);
            startAlarm(time, date, todo, id);
            todoViewModel.update(todo);

            Toast.makeText(getActivity(), getResources().getString(R.string.todo_updated_successed)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.todo_not_saved)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTodoClick(Todo currentTodo) {

        if (dualPane) {
            TodoDescriptionFragment todoDescriptionFragment = new TodoDescriptionFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TodoDescriptionFragment.BUNDLE_TITLE, currentTodo.getTitle());
            bundle.putString(TodoDescriptionFragment.BUNDLE_DESCRIPTION,
                    currentTodo.getDescription());
            bundle.putString(TodoDescriptionFragment.BUNDLE_LOCATION,
                    currentTodo.getLocation());
            bundle.putLong(TodoDescriptionFragment.BUNDLE_TIME, currentTodo.getTime());
            bundle.putLong(TodoDescriptionFragment.BUNDLE_DATE, currentTodo.getDate());
            bundle.putLong(TodoDescriptionFragment.BUNDLE_REMINDER, currentTodo.getReminderTime());
            todoDescriptionFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details, todoDescriptionFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), DescriptionActivity.class);
            intent.putExtra(TodoDescriptionFragment.BUNDLE_TITLE, currentTodo.getTitle());
            intent.putExtra(TodoDescriptionFragment.BUNDLE_DESCRIPTION, currentTodo.getDescription());
            intent.putExtra(TodoDescriptionFragment.BUNDLE_LOCATION, currentTodo.getLocation());
            intent.putExtra(TodoDescriptionFragment.BUNDLE_TIME, currentTodo.getTime());
            intent.putExtra(TodoDescriptionFragment.BUNDLE_DATE, currentTodo.getDate());
            intent.putExtra(TodoDescriptionFragment.BUNDLE_REMINDER, currentTodo.getReminderTime());
            startActivity(intent);
        }
    }

    @Override
    public void onFavClick(Todo todo) {
        if (todo.getPriority()) {
            todo.setPriority(false);
        } else {
            todo.setPriority(true);
        }
        todoViewModel.update(todo);
        Toast.makeText(getActivity(), getResources().getString(R.string.todo_priority),
                Toast.LENGTH_SHORT).show();
    }

    private void startAlarm(long time, long date, Todo todo, long todoId) {

        Calendar calTime = Calendar.getInstance();
        calTime.setTimeInMillis(time);

        Calendar calDate = Calendar.getInstance();
        calDate.setTimeInMillis(date);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH),
                calDate.get(Calendar.DAY_OF_MONTH), calTime.get(Calendar.HOUR_OF_DAY),
                calTime.get(Calendar.MINUTE));

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("todoTitle", todo.getTitle());
        intent.putExtra("notificationId", todoId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), todo.getId(),
                intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(
                Context.ALARM_SERVICE);

        long triggerTime = (calendar.getTimeInMillis() - (todo.getReminderTime() * 60000L));

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                triggerTime, pendingIntent);
    }


    private void cancelAlarm(Todo todo, long id) {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("todoTitle", todo.getTitle());
        intent.putExtra("notificationId", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), todo.getId(),
                intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(
                Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }
}
