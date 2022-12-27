package demo.Algorithem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuickSort {

    public static void main(String[] args) {
        int[] array = {15,9,7,13,12,16,4,18,11};

        int length = array.length;

        QuickSort quickSort = new QuickSort();
        quickSort.quickSortRecursion(array,0,length-1);
        quickSort.printArray(array);

    }

    int partition(int[] array , int lower , int higher){
        int pivot = array[(lower+higher)/2];
        while (lower <= higher){
            while (array[lower] < pivot){
                lower++;
            }
            while (array[higher] > pivot){
                higher--;
            }
            if (lower<=higher){
                int temp = array[lower];
                array[lower] = array[higher];
                array[higher] =temp;

                lower++;
                higher--;
            }
        }
        return lower;
    }

    void quickSortRecursion(int[] array , int lower , int higher){
        int pi = partition(array,lower, higher);
        if (lower<pi-1){
            quickSortRecursion(array,lower,pi-1);
        }
        if (pi < higher){
            quickSortRecursion(array,pi,higher);
        }
    }

    void printArray(int[] array){
        for (int i : array) {
            log.info(i+" ");
        }
    }
}
