/*
package demo;

import java.util.*;

public class Solutions {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Medicine> medicines = new ArrayList<>();
        medicines= (List<Medicine>) new Medicine(scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextInt());
        scanner.nextLine();

        String disease = scanner.nextLine();
        scanner.close();

        getPriceByDisease(medicines,disease);
    }

    public static void getPriceByDisease(List<Medicine> medicineList, String disease){

        List<Medicine> medicines = new ArrayList<>();
        for (Medicine medicine : medicineList) {
            medicine.setDisease(medicine.getDisease());
            if (medicine.equals(disease)) {
                medicine.setPrice(medicine.price);
                medicines.set(medicines.size() - 1, medicine.getPrice());
            }
        }
    }

}
*/
