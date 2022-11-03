/*
package demo;

import java.util.*;

public class MyClass {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = new ArrayList<>(4);

        for (Employee employee : employees) {
            employee=new Employee(scanner.nextInt(), scanner.nextLine(),scanner.nextLine(),scanner.nextDouble(),scanner.nextBoolean());
            scanner.nextLine();
        }

        String branch = scanner.nextLine();

        List<Employee> strings =findCountOfEmployeesUsingCompTransport(employees, branch);
        for (Employee string : strings) {
            System.out.println(string);
        }
        double employeeRating = scanner.nextDouble();
        boolean companyTransport = scanner.nextBoolean();
            List<Employee> employees1 = findEmployeeWithSecondHighestRating(employees, employeeRating, companyTransport);
            for (Employee employee : employees1) {
                System.out.println(employee);
        }

    }
    public static List<Employee> findCountOfEmployeesUsingCompTransport(List<Employee> employees, String branch){

        List<String> employees1 = new ArrayList<>(0);
        int count = 0;
        for (Employee employee : employees) {
            if (employee.equals(branch)) {
                if (employee.isCompanyTransport()) {
                    count = employee.getEmployeeId();
                    employees1.add(String.valueOf(count));
                    count+=1;
                }
            }
        }
        return count;
    }

    public static List<Boolean> findEmployeeWithSecondHighestRating(Employee[] employees, double employeeRating, boolean companyTransport){
        Employee[] employee = new Employee[0];
        //new list transport false store
        //sort rating descending
        //last second number

       Arrays.sort(new Comparator[]{Comparator.comparing(Employee::getEmployeeRating)});
 int secondLargestRating = 0;
        for (int i = (int) (employeeRating-2);i>0;i--){
            if (employees[i]!=employees[(int) (employeeRating-1)]){
                secondLargestRating= employees[i].getEmployeeId();
            }
        }

 for (Employee employee1 : employees) {
            if (employee1.equals(employee)){

            }
        }


        List<Boolean> employeeList = new ArrayList<>();
        if (!companyTransport){
            employeeList.add(false);
        }

        Collections.sort(employeeList, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o2.getEmployeeRating()-o1.getEmployeeRating();
            }
        });
        if (employeeList.size()>1){
            System.out.println(employeeList.get(1));
        }

        return employeeList;
    }


    public static Integer[] findEmployeeWithSecondHighestRatings(Employee[] employees,double employeeRating, boolean companyTransport){
        Integer[] integers = new Integer[0];
        for (int i=0;i<integers.length;i++){
            if (!companyTransport){
                integers=Arrays.copyOf(integers,integers.length+1);
                integers[integers.length-1]= Integer.valueOf(employees[i].getEmployeeName());
            }
            Arrays.sort(integers);
        }
        return integers;
    }



}
*/
