package model;

import java.util.ArrayList;
import java.util.List;

//counselor turunan user, berisi gelar, hari kerja, dan jadwal konseling.
public class Counselor extends User {
    private String title;              //gelar (misal: S.Psi, M.Psi)
    private List<String> workDays;     //hari kerja 
    private List<String> officeHours;  //jam kerja konseling (jadwal konseling)
    
    public Counselor(String id, String name, String title) {
        super(id, name);
        this.title = title;
        this.workDays = new ArrayList<>();
        this.officeHours = new ArrayList<>();
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<String> getWorkDays() {
        return workDays;
    }
    
    public void addWorkDay(String day) {
        if (day == null || day.isBlank()) {
            throw new IllegalArgumentException("Hari tidak boleh kosong");
        }
        if (!workDays.contains(day)) {
            workDays.add(day);
        }
    }
    
    public boolean isAvailableOnDay(String day) {
        return workDays.contains(day);
    }
    
    public List<String> getOfficeHours() {
        return officeHours;
    }
    
    public void addOfficeHour(String time) {
        if (time == null || time.isBlank()) {
            throw new IllegalArgumentException("Jam tidak boleh kosong");
        }
        if (!officeHours.contains(time)) {
            officeHours.add(time);
        }
    }
    
    public String getFullName() {
        return "Dr. " + getName() + " (" + title + ")";
    }
    
    @Override
    public String toString() {
        return getId() + " - " + getName() + ", " + title;
    }
}
