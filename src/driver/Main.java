package driver;

import model.Appointment;
import model.Counselor;
import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<String> CAMPUS_HOURS = List.of("08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00");
    private static final List<String> AVAILABLE_DAYS = List.of("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
    private static final String[] HOUR_OPTIONS = {"08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Mental Health Automated Matcher dengan Multiple Konselor ===\n");
        
        //inisialisasi 3 konselor
        List<Counselor> counselors = new ArrayList<>();
        
        Counselor c1 = new Counselor("C001", "Siti", "S.Psi, M.Psi");
        c1.addWorkDay("Senin");
        c1.addWorkDay("Rabu");
        c1.addWorkDay("Jumat");
        c1.addOfficeHour("09:00");
        c1.addOfficeHour("11:00");
        c1.addOfficeHour("14:00");
        counselors.add(c1);
        
        Counselor c2 = new Counselor("C002", "Rini", "S.Psi");
        c2.addWorkDay("Senin");
        c2.addWorkDay("Selasa");
        c2.addWorkDay("Kamis");
        c2.addOfficeHour("08:00");
        c2.addOfficeHour("10:00");
        c2.addOfficeHour("15:00");
        counselors.add(c2);
        
        Counselor c3 = new Counselor("C003", "Budi", "S.Psi, Ph.D");
        c3.addWorkDay("Selasa");
        c3.addWorkDay("Rabu");
        c3.addWorkDay("Jumat");
        c3.addOfficeHour("08:00");
        c3.addOfficeHour("13:00");
        c3.addOfficeHour("16:00");
        counselors.add(c3);

        //inputan student
        System.out.print("Masukkan NIM mahasiswa: ");
        String studentId = scanner.nextLine().trim();
        System.out.print("Masukkan nama mahasiswa: ");
        String studentName = scanner.nextLine().trim();

        System.out.print("Masukkan keluhan/alasan konseling: ");
        String complain = scanner.nextLine().trim();

        System.out.println("\n=== Pilih Jam Kuliah Mahasiswa ===");
        System.out.println("1. 08:00    4. 11:00    7. 15:00");
        System.out.println("2. 09:00    5. 13:00    8. 16:00");
        System.out.println("3. 10:00    6. 14:00    9. 17:00");
        System.out.print("Masukkan pilihan jam (pisahkan dengan koma, misal: 1,3,5): ");
        String studentBusyInput = scanner.nextLine().trim();
        StringBuilder studentBusy = new StringBuilder();
        for (String choice : studentBusyInput.split(",")) {
            try {
                int idx = Integer.parseInt(choice.trim()) - 1;
                if (idx >= 0 && idx < HOUR_OPTIONS.length) {
                    if (studentBusy.length() > 0) studentBusy.append(",");
                    studentBusy.append(HOUR_OPTIONS[idx]);
                }
            } catch (NumberFormatException e) {}
        }

        Student student = new Student(studentId.isEmpty() ? "S001" : studentId, studentName.isEmpty() ? "Tidak Diketahui" : studentName);
        parseSlots(studentBusy.toString()).forEach(student::addSchedule);
        student.setComplain(complain);

        //tampilkan daftar konselor
        System.out.println("\n=== Daftar Konselor Tersedia ===");
        for (int i = 0; i < counselors.size(); i++) {
            Counselor c = counselors.get(i);
            System.out.println((i + 1) + ". " + c.getFullName());
        }

        //student pilih konselor
        System.out.print("\nPilih konselor (1-3): ");
        int counselorChoice = 0;
        try {
            counselorChoice = Integer.parseInt(scanner.nextLine().trim());
            if (counselorChoice < 1 || counselorChoice > 3) {
                System.out.println("Pilihan tidak valid. Menggunakan konselor 1.");
                counselorChoice = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Menggunakan konselor 1.");
            counselorChoice = 1;
        }
        
        Counselor selectedCounselor = counselors.get(counselorChoice - 1);

        //student input hari konseling (dengan validasi loop)
        String requestDay;

        while (true) {
            System.out.print("Masukkan hari konseling yang diinginkan (Senin/Selasa/Rabu/Kamis/Jumat): ");
            String inputDay = scanner.nextLine().trim();

            // Cek case-insensitive
            String validDay = null;
            for (String day : AVAILABLE_DAYS) {
                if (day.equalsIgnoreCase(inputDay)) {
                    validDay = day;
                    break;
                }
            }

            if (validDay != null) {
                requestDay = validDay;
                break; // keluar kalau valid
            } else {
                System.out.println("❌ Hari tidak valid, silakan coba lagi.");
            }
        }

        student.setRequestDay(requestDay);
        //matching: jam cocok + hari cocok
        Appointment appointment = findBestSlot(student, selectedCounselor);
        
        if (appointment != null) {
            System.out.println("\n" + appointment);
        } else {
            System.out.println("\n❌ Tidak ada slot yang cocok dengan preferensi Anda pada hari " + student.getRequestDay() + ".");
            
            // Tampilkan jadwal lengkap konselor di hari yang sama
            System.out.println("\n=== 📅 Jadwal Konselor pada " + student.getRequestDay() + " ===");
            displayCounselorSchedulesOnDay(counselors, student, student.getRequestDay());
            
            System.out.println("\n" + "=".repeat(50));
            
            //tampilkan alternatif pada hari request (same-day)
            List<Appointment> alternatives = findAlternativeSlots(student, counselors, selectedCounselor);
            
            if (!alternatives.isEmpty()) {
                // Separate same-day and other-day alternatives
                List<Appointment> sameDayAlts = new ArrayList<>();
                List<Appointment> otherDayAlts = new ArrayList<>();
                
                for (Appointment alt : alternatives) {
                    if (alt.getDay().equals(student.getRequestDay())) {
                        sameDayAlts.add(alt);
                    } else {
                        otherDayAlts.add(alt);
                    }
                }
                
                // Show same-day alternatives first
                if (!sameDayAlts.isEmpty()) {
                    System.out.println("\n=== 💡 Alternatif Jadwal Hari " + student.getRequestDay() + " ===");
                    for (int i = 0; i < sameDayAlts.size(); i++) {
                        Appointment alt = sameDayAlts.get(i);
                        System.out.println((i + 1) + ". " + alt.getCounselor().getFullName());
                        System.out.println("   └─ Jam: " + alt.getSlotTime() + " | Hari: " + alt.getDay());
                    }
                }
                
                // Show other-day alternatives if any
                if (!otherDayAlts.isEmpty()) {
                    System.out.println("\n=== Alternatif Jadwal Hari Lain ===");
                    int startIdx = sameDayAlts.size();
                    for (int i = 0; i < otherDayAlts.size(); i++) {
                        Appointment alt = otherDayAlts.get(i);
                        System.out.println((startIdx + i + 1) + ". " + alt.getCounselor().getFullName());
                        System.out.println("   └─ Jam: " + alt.getSlotTime() + " | Hari: " + alt.getDay());
                    }
                }
                
                System.out.print("\nPilih alternatif (1-" + alternatives.size() + "), atau 0 untuk tidak: ");
                try {
                    int altChoice = Integer.parseInt(scanner.nextLine().trim());
                    if (altChoice > 0 && altChoice <= alternatives.size()) {
                        Appointment selectedAlt = alternatives.get(altChoice - 1);
                        System.out.println("\n✅ Jadwal Anda telah dikonfirmasi:\n");
                        System.out.println(selectedAlt);
                    } else if (altChoice != 0) {
                        System.out.println("❌ Pilihan tidak valid.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Input tidak valid.");
                }
            } else {
                System.out.println("❌ Tidak ada alternatif yang tersedia.");
            }
        }

        scanner.close();
    }

    private static List<String> parseSlots(String raw) {
        List<String> slots = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return slots;
        }
        for (String token : raw.split(",")) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                slots.add(trimmed);
            }
        }
        return slots;
    }

    private static Appointment findBestSlot(Student student, Counselor counselor) {
        String requestedDay = student.getRequestDay();

        if (!counselor.isAvailableOnDay(requestedDay)) {
            System.out.println("Konselor tidak bekerja pada hari " + requestedDay + ".");
            return null;
        }

        for (String slot : CAMPUS_HOURS) {
            boolean studentBusy = student.getSchedule().contains(slot);
            boolean counselorAvailable = counselor.getOfficeHours().contains(slot);

            if (!studentBusy && counselorAvailable) {
                return new Appointment(student, counselor, slot, requestedDay);
            }
        }

        return null;
    }
    
    private static List<Appointment> findAlternativeSlots(Student student, List<Counselor> allCounselors, Counselor selectedCounselor) {
        List<Appointment> sameDayAlts = new ArrayList<>();
        List<Appointment> otherDayAlts = new ArrayList<>();
        String requestDay = student.getRequestDay();

        // 1) Cari SEMUA slot dari KONSELOR LAIN pada hari request
        for (Counselor counselor : allCounselors) {
            if (counselor.getId().equals(selectedCounselor.getId())) {
                continue;
            }

            if (!counselor.isAvailableOnDay(requestDay)) {
                continue;
            }

            for (String slot : CAMPUS_HOURS) {
                if (!student.getSchedule().contains(slot) && counselor.getOfficeHours().contains(slot)) {
                    sameDayAlts.add(new Appointment(student, counselor, slot, requestDay));
                }
            }
        }

        // 2) Jika belum ada same-day, cari dari KONSELOR TERPILIH pada hari request
        if (sameDayAlts.isEmpty()) {
            if (selectedCounselor.isAvailableOnDay(requestDay)) {
                for (String slot : CAMPUS_HOURS) {
                    boolean studentFree = !student.getSchedule().contains(slot);
                    boolean counselorHas = selectedCounselor.getOfficeHours().contains(slot);
                    if (studentFree && counselorHas) {
                        sameDayAlts.add(new Appointment(student, selectedCounselor, slot, requestDay));
                    }
                }
            }
        }

        // 3) SELALU cari backup dari hari lain (max 3) untuk ditampilkan
        int backupCount = 0;
        for (Counselor counselor : allCounselors) {
            if (backupCount >= 3) break;

            for (String day : AVAILABLE_DAYS) {
                if (backupCount >= 3) break;
                
                if (day.equalsIgnoreCase(requestDay)) {
                    continue;
                }

                if (!counselor.isAvailableOnDay(day)) {
                    continue;
                }

                for (String slot : CAMPUS_HOURS) {
                    if (!student.getSchedule().contains(slot) && counselor.getOfficeHours().contains(slot)) {
                        otherDayAlts.add(new Appointment(student, counselor, slot, day));
                        backupCount++;
                        break;
                    }
                }
            }
        }

        // Gabung: same-day dulu, kemudian other-day
        sameDayAlts.addAll(otherDayAlts);
        return sameDayAlts;
    }

    private static List<String> getAvailableHours(Student student) {
        List<String> available = new ArrayList<>();
        for (String hour : CAMPUS_HOURS) {
            if (!student.getSchedule().contains(hour)) {
                available.add(hour);
            }
        }
        return available;
    }
    
    private static void displayCounselorSchedulesOnDay(List<Counselor> counselors, Student student, String day) {
        System.out.println("");
        for (Counselor counselor : counselors) {
            if (counselor.isAvailableOnDay(day)) {
                System.out.println("👤 " + counselor.getFullName() + " (" + counselor.getTitle() + ")");
                System.out.println("   ID: " + counselor.getId());
                System.out.print("   Jadwal Tersedia: ");
                
                List<String> availableSlots = new ArrayList<>();
                for (String hour : counselor.getOfficeHours()) {
                    boolean studentBusy = student.getSchedule().contains(hour);
                    String slotMarker = studentBusy ? "❌ " + hour : "✅ " + hour;
                    availableSlots.add(slotMarker);
                }
                System.out.println(String.join(" | ", availableSlots));
                System.out.println("");
            }
        }
    }
}
