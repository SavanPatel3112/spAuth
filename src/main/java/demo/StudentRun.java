package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
@Slf4j
public class StudentRun {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        log.info("Enter the number how many student details you have add ::");
        int size = scanner.nextInt();
        List<Student> studentList = new ArrayList<>();
        for (int i=0;i<size;i++) {
            int id = scanner.nextInt();
            String firstName = scanner.next();
            double CGPA = Double.parseDouble(scanner.next());
            studentList.add(new Student(id, firstName, CGPA));
        }
        scanner.nextLine();
        sortStudentList(studentList);
    }
    public static void sortStudentList(List<Student> studentList){
        studentList.sort((s1, s2) -> {
            if (s1.getCGPA() < s2.getCGPA()) {
                return 1;
            } else if (s1.getCGPA() > s2.getCGPA()) {
                return -1;
            } else if (s1.getId() == s2.getId()) {
                return 1;
            }
            return s2.getFirstName().compareTo(s1.getFirstName());
        });
        for (Student student : studentList) {
            log.info("Students Name:{}",student.getFirstName());
        }
    }
}

