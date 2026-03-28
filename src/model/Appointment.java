package model;

//relasi antara student dan counselor pada slot waktu dan hari tertentu
public class Appointment {
    private final Student student;
    private final Counselor counselor;
    private final String slotTime;
    private final String day;  // hari konseling

    public Appointment(Student student, Counselor counselor, String slotTime, String day) {
        this.student = student;
        this.counselor = counselor;
        this.slotTime = slotTime;
        this.day = day;
    }

    public Student getStudent() {
        return student;
    }

    public Counselor getCounselor() {
        return counselor;
    }

    public String getSlotTime() {
        return slotTime;
    }
    
    public String getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "JADWAL KONSELING\n" +
                "================\n" +
                "Mahasiswa\t: " + student.getName() + " (" + student.getId() + ")\n" +
                "Konselor\t: " + counselor.getFullName() + "\n" +
                "Keluhan\t\t: " + student.getComplain() + "\n" +
                "Hari\t\t: " + day + "\n" +
                "Jam\t\t: " + slotTime;
    }
}
