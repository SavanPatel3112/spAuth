package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
@Slf4j
public class MyClasss {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Employee> employeeList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int employeeId=scanner.nextInt();
            String employeeName=scanner.next();
            String employeeBranch=scanner.next();
            double employeeRating=scanner.nextDouble();
            boolean companyTransport= Boolean.parseBoolean(scanner.next());
            employeeList.add(new Employee(employeeId,employeeName,employeeBranch,employeeRating,companyTransport));
            scanner.nextLine();
        }
        String branch = scanner.next();
        boolean companyTransports = scanner.nextBoolean();
        int integers1 =findCountOfEmployeesUsingCompTransport(employeeList,branch);
        if (integers1==0)
            log.info("No such Employees");
        else
            log.info("input:{}",integers1);

        findEmployeeWithSecondHighestRatings(employeeList, companyTransports);
    }

    public static int findCountOfEmployeesUsingCompTransport(List<Employee> employees,String branch){

        int count = 0;
        for (Employee employee : employees) {
            if (employee.getEmployeeBranch().equals(branch)&& employee.isCompanyTransport()){
                count++;
            }
        }
        return count;
    }

    public static void findEmployeeWithSecondHighestRatings(List<Employee> employees, boolean companyTransport){

        List<Employee> employeeList= new ArrayList<>();

        for (Employee employee : employees) {
            if (!employee.isCompanyTransport()){
                employeeList.add(employee);
            }
        }
        Collections.sort(employeeList, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return (int) (o2.getEmployeeRating()-o1.getEmployeeRating());
            }
        });
        if (employeeList.size()>1){
            log.info("employee:{}",employeeList.get(1));
        }else {
            log.info("employee:{}",employeeList.get(employeeList.size()-1));
        }



/*
        Integer[] integers = new Integer[0];
        for (int i=0;i<integers.length;i++){
            if (!companyTransport){
                integers= Arrays.copyOf(integers,integers.length+1);
                integers[integers.length-1]= employees[i].getEmployeeId();
            }
            Arrays.sort(integers);
        }
        List<Employee> employeeList= new ArrayList<>();
        Collections.sort(employeeList, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return (int) (o2.getEmployeeRating()-o1.getEmployeeRating());
            }
        });
        if (employeeList.size()>1){
            System.out.println(employeeList.get(1));
        }
        return integers;
*/
    }
}
