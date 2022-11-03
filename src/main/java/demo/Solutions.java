package demo;

import java.util.Arrays;
import java.util.Scanner;

public class Solutions {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Medicine[] medicines = new Medicine[2];

        for (int i=0;i<medicines.length;i++){
            medicines[i] = new Medicine(scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextInt());
            scanner.nextLine();
        }

        String disease = scanner.nextLine();
        scanner.close();

        Integer[] integers = getPriceByDisease(medicines,disease);
        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

    public static Integer[] getPriceByDisease(Medicine[] medicines,String disease){

        Integer[] integers = new Integer[0];
        for (int i=0;i<integers.length;i++){

            if (medicines[i].getDisease().equals(disease)) {
                integers = Arrays.copyOf(integers, integers.length+1);
                integers[integers.length-1] = medicines[i].getPrice();
            }
        }
        return integers;
    }

}
