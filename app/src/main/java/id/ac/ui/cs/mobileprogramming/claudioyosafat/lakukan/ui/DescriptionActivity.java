package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.R;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.databinding.ActivityDescriptionBinding;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.fragment.TodoDescriptionFragment;

public class DescriptionActivity extends FragmentActivity {

    ActivityDescriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
//        if(getResources().getConfiguration().orientation ==
//                Configuration.ORIENTATION_LANDSCAPE) {
//            finish();
//            return;
//        }

        if(savedInstanceState == null) {
            TodoDescriptionFragment todoDescriptionFragment = new TodoDescriptionFragment();
            todoDescriptionFragment.setArguments(getIntent().getExtras());
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.details, todoDescriptionFragment);
            fragmentTransaction.commit();
        }
    }
}
