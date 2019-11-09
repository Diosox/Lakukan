package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.R;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data.Todo;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.databinding.TodoDescriptionFragmentBinding;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.viewmodel.TodoViewModel;

public class TodoDescriptionFragment extends Fragment {

    public static final String BUNDLE_TITLE =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.BUNDLE_TITLE";
    public static final String BUNDLE_DESCRIPTION =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.BUNDLE_DESCRIPTION";
    public static final String BUNDLE_TIME =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.BUNDLE_TIME";
    public static final String BUNDLE_DATE =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.BUNDLE_DATE";
    public static final String BUNDLE_LOCATION =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.BUNDLE_LOCATION";
    public static final String BUNDLE_REMINDER =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.BUNDLE_REMINDER";

    private TodoDescriptionFragmentBinding binding;
    private TodoViewModel todoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.todo_description_fragment, container, false);
        View descriptionView = binding.getRoot();
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Todo todo = new Todo(bundle.getString(BUNDLE_TITLE, "noTitle")
                    , bundle.getString(BUNDLE_DESCRIPTION, "noDescription"),
                    false, false, bundle.getLong(BUNDLE_TIME, 0L),
                    bundle.getLong(BUNDLE_DATE, 0L),
                    bundle.getString(BUNDLE_LOCATION, "noLocation"),
                    bundle.getLong(BUNDLE_REMINDER, 0L));

            binding.setTodo(todo);

            Calendar calTime = Calendar.getInstance();
            Calendar calDate = Calendar.getInstance();

            calTime.setTimeInMillis(bundle.getLong(BUNDLE_TIME, 0L));
            calDate.setTimeInMillis(bundle.getLong(BUNDLE_DATE, 0L));

            binding.descFragmentDateTime.setText(calTime.get(Calendar.HOUR_OF_DAY) + ":"
                    + calTime.get(Calendar.MINUTE) + ", " + calDate.get(Calendar.DAY_OF_MONTH) +
                    "/" + calDate.get(Calendar.MONTH) + "/" + calDate.get(Calendar.YEAR)
            );

            binding.descFragmentReminder.setText(bundle.getLong(BUNDLE_REMINDER)
                    + " minutes before");
        }

        return descriptionView;
    }
}
