package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String location;
    private Boolean isDone;
    private Boolean isPriority;
    private long time;
    private long date;
    private long reminderTime;

    public Todo(String title, String description, Boolean isDone, Boolean isPriority, long time,
                long date, String location, long reminderTime) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.isPriority = isPriority;
        this.time = time;
        this.date = date;
        this.location = location;
        this.reminderTime = reminderTime;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public void setPriority(Boolean priority) {
        isPriority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDone() {
        return isDone;
    }

    public Boolean getPriority() {
        return isPriority;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }
}
