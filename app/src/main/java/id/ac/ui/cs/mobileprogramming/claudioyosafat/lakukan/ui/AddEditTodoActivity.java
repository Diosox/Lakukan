package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.R;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.databinding.ActivityAddTodoBinding;
import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.viewmodel.TodoViewModel;

public class AddEditTodoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ID =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_TIME";
    public static final String EXTRA_DATE =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_DATE";
    public static final String EXTRA_LOCATION =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_LOCATION";
    public static final String EXTRA_REMINDER =
            "id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.EXTRA_REMINDER";


    private ActivityAddTodoBinding binding;
    private EditText editTextTime, editTextDate;
    private Spinner spinner;
    private int year, month, day, hour, minute;
    private long time, date, reminderTime;
    private ArrayAdapter<Long> reminderTimeAdapter;


    private TodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_todo);

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        editTextTime = findViewById(R.id.edit_text_time);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextTime.setOnClickListener(this);
        editTextDate.setOnClickListener(this);

        addItemsOnSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                reminderTime = Long.parseLong(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                reminderTime = 0L;
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Todo");

            Calendar calTime = Calendar.getInstance();
            Calendar calDate = Calendar.getInstance();

            calTime.setTimeInMillis(intent.getLongExtra(EXTRA_TIME, 0L));
            calDate.setTimeInMillis(intent.getLongExtra(EXTRA_DATE, 0L));

            int spinnerPosition = reminderTimeAdapter.getPosition(intent.getLongExtra(EXTRA_REMINDER,
                    0L));

            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            binding.editTextLocation.setText(intent.getStringExtra(EXTRA_LOCATION));
            binding.editTextTime.setText(calTime.get(Calendar.HOUR_OF_DAY) + ":" +
                    calTime.get(Calendar.MINUTE));
            binding.editTextDate.setText(calDate.get(Calendar.DAY_OF_MONTH) + "-" +
                    (calDate.get(Calendar.MONTH) + 1) + "-" + calDate.get(Calendar.YEAR));
            binding.spinner.setSelection(spinnerPosition);

            time = calTime.getTimeInMillis();
            date = calDate.getTimeInMillis();

        } else {
            setTitle("Add Todo");
        }
    }

    private void saveTodo() {
        Calendar c = Calendar.getInstance();
        long currenTime = c.getTimeInMillis();

        if (time >= currenTime) {
            String title = binding.editTextTitle.getText().toString();
            String description = binding.editTextDescription.getText().toString();
            String location = binding.editTextLocation.getText().toString();


            if (title.trim().isEmpty() || description.trim().isEmpty() ||
                    location.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a title, description, and location"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_LOCATION, location);
            data.putExtra(EXTRA_TIME, time);
            data.putExtra(EXTRA_DATE, date);
            data.putExtra(EXTRA_REMINDER, reminderTime);

            int id = getIntent().getIntExtra(EXTRA_ID, -1);

            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, data);
            finish();
        } else {
            Toast.makeText(this, "Invalid time", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_todo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_todo:
                saveTodo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == editTextDate) {

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int dateYear, int dateMonth,
                                              int dateDay) {
                            binding.editTextDate.setText(dateDay + "-" + (dateMonth + 1) + "-" +
                                    dateYear);
                            c.set(dateYear, dateMonth, dateDay);
                            date = c.getTimeInMillis();
                        }
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();

        }
        if (view == editTextTime) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int timeHour, int timeMinute) {
                            binding.editTextTime.setText(timeHour + ":" + timeMinute);
                            c.set(Calendar.HOUR_OF_DAY, timeHour);
                            c.set(Calendar.MINUTE, timeMinute);
                            time = c.getTimeInMillis();
                        }
                    }, hour, minute, true);
            timePickerDialog.show();
        }
    }

    private void addItemsOnSpinner() {

        spinner = findViewById(R.id.spinner);
        List<Long> timeList = new ArrayList<Long>();
        timeList.add(0L);
        timeList.add(1L);
        timeList.add(5L);
        timeList.add(10L);
        timeList.add(15L);
        timeList.add(30L);
        timeList.add(60L);

        reminderTimeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, timeList);
        spinner.setAdapter(reminderTimeAdapter);
    }
}
