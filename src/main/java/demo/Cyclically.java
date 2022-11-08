package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Cyclically {

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
        int count = 0;
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < myArray.length; i++) {
            if (i >k) {
                numbers.add(myArray[i]);
                count++;
            }
        }
        for (int i = 0; i <= count; i++) {
            numbers.add(myArray[i]);
        }

/*        for (int j = myArray.length - 1; j >= 0; j--) {
            log.info("vallue:{}", myArray[j]);
            if (count < k) {
                nummbers.add(myArray[j]);
                count++;
            } else {
                nummbers.add(myArray[j]);
            }*/

                log.info(" cycled array: {}", numbers);
 /*           for (int i : myArray) {
                if (i < k) {
                    nummbers.add(i);
                } else {
                    nummbers.add(i);
                }
            }*/

    /*    for(int i=0;i<k;i++)
        {
            int last =myArray[i];
            for(int j=0;j<myArray.length-1;j++)
            {
                myArray[j]=myArray[j+1];
            }
            myArray[myArray.length-1]= last;
        }
        for (int j : myArray) {
            log.info("Cyclically array:{}",myArray[j]);*/
            }


}
