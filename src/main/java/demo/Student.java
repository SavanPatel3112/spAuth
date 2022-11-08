package demo;

public class Student {

    int id;
    String firstName;
    double CGPA;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public double getCGPA() {
        return CGPA;
    }

    public void setCGPA(double CGPA) {
        this.CGPA = CGPA;
    }

    public Student(int id, String firstName, double CGPA) {
        this.id = id;
        this.firstName = firstName;
        this.CGPA = CGPA;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", CGPA=" + CGPA +
                '}';
    }
}
