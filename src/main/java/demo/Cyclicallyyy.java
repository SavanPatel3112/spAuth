package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
@Slf4j
public class Cyclicallyyy {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        log.info("Enter the required size of the array ::");
        int size = scanner.nextInt();
        int[] myArray = new int[size];
        log.info("Enter elements of the array");
        for (int i = 0; i < size; i++) {
            myArray[i] = scanner.nextInt();
        }
        log.info("Enter the value of K");
        int k = scanner.nextInt();
        log.info(Arrays.toString(myArray));
        int numbers;
        //Rotate the given myArray by k times go to right
        for(int i = 0; i < k; i++){

            numbers=myArray[myArray.length-1];
            /*numbers.add(myArray.length-1);*/

            for (int j =myArray.length-1;j>0;j--){

                myArray[j]=myArray[j-1];
            }
            /*numbers.add(myArray[0]);*/
            myArray[0]=numbers;

        }
        for (int i=0;i<myArray.length;i++){
            log.info(String.valueOf((myArray[i])));
        }

        }

}
