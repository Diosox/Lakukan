package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.R;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data.Todo;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.databinding.TodoItemBinding;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.OnFavClickListener;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.OnTodoClickListener;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {
    private List<Todo> todos = new ArrayList<>();
    private Context ctx;
    private OnTodoClickListener todoListener;
    private OnFavClickListener favListener;

    public TodoAdapter(Context ctx, OnTodoClickListener todoListener,
                       OnFavClickListener favListener) {
        this.ctx = ctx;
        this.todoListener = todoListener;
        this.favListener = favListener;
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodoItemBinding todoItemBinding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.todo_item, parent, false);

        return new TodoHolder(todoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoHolder holder, int position) {
        final Todo currentTodo = todos.get(position);
        holder.todoItemBinding.setTodo(currentTodo);

        Calendar calTime = Calendar.getInstance();
        Calendar calDate = Calendar.getInstance();

        calTime.setTimeInMillis(currentTodo.getTime());
        calDate.setTimeInMillis(currentTodo.getDate());

        holder.todoItemBinding.textViewDate.setText("( " + calTime.get(Calendar.HOUR_OF_DAY) + ":"
                + calTime.get(Calendar.MINUTE) + ", " + calDate.get(Calendar.DAY_OF_MONTH) +
                "/" + calDate.get(Calendar.MONTH) + "/" + calDate.get(Calendar.YEAR) + " )"
        );

        holder.todoItemBinding.isFavorite.setChecked(currentTodo.getPriority());

        holder.bind(currentTodo, todoListener);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    class TodoHolder extends RecyclerView.ViewHolder {
        private final TodoItemBinding todoItemBinding;

        public TodoHolder(@NonNull TodoItemBinding todoItemBinding) {
            super(todoItemBinding.getRoot());
            this.todoItemBinding = todoItemBinding;
        }

        public void bind(final Todo todo, final OnTodoClickListener listener) {
            todoItemBinding.setVariable(BR.todo, todo);
            todoItemBinding.executePendingBindings();

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onTodoClick(todo);
                }
            });

            todoItemBinding.isFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favListener.onFavClick(todo);
                }
            });
        }

    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    public Todo getTodoAt(int position) {
        return todos.get(position);
    }

}
