package model;

//student adalah turunan dari user dengan hari request konseling dan alasan/keluhan.
public class Student extends User {
    private String requestDay;  //hari mahasiswa ingin konseling
    private String complain;    //keluhan atau alasan konseling

    public Student(String id, String name) {
        super(id, name);
        this.requestDay = "";
        this.complain = "";
    }
    
    public String getRequestDay() {
        return requestDay;
    }
    
    public void setRequestDay(String day) {
        if (day == null || day.isBlank()) {
            throw new IllegalArgumentException("Hari tidak boleh kosong");
        }
        this.requestDay = day;
    }
    
    public String getComplain() {
        return complain;
    }
    
    public void setComplain(String complain) {
        if (complain != null && !complain.isBlank()) {
            this.complain = complain;
        } else {
            this.complain = "-";
        }
    }
}
