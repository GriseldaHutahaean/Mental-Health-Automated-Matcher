package model;

import java.util.ArrayList;
import java.util.List;

//kelas abstrak dasar untuk Student dan Counselor
//setiap user punya id, nama, dan jam kerja
public abstract class User {
    private String id;
    private String name;
    private final List<String> schedule;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.schedule = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public void addSchedule(String time) {
        if (time == null || time.isBlank()) {
            throw new IllegalArgumentException("Jadwal tidak boleh kosong");
        }
        schedule.add(time);
    }
}
