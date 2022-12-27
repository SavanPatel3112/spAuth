package demo.Algorithem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MergeSort {

    int[] array;
    int[] tempMergeArr;
    int length;
    public static void main(String[] args) {

        int[] inputArr = {48,36,13,52,19,94,21};

        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(inputArr);

        for (int i:inputArr){
            log.info(i+" ");
        }
    }

    public void sort(int[] inputArr){
        this.array=inputArr;
        this.length=inputArr.length;
        this.tempMergeArr=new int[length];
        divideArray(0,length-1);
    }

    public void divideArray(int lowerIndex , int higherIndex){
        if (lowerIndex<higherIndex){
            int middleIndex = lowerIndex+(higherIndex-lowerIndex)/2;
            //it will sort the lest side of an array
            divideArray(lowerIndex, middleIndex);
            //it will sort ht e right side an array
            divideArray(middleIndex +1,higherIndex);
            mergeArray(lowerIndex, middleIndex,higherIndex);
        }
    }
    public void mergeArray(int lowerIndex , int middleIndex , int higherIndex ){
        for (int i=lowerIndex;i<=higherIndex;i++){
            tempMergeArr[i] = array[i];
        }
        int i=lowerIndex;
        int j=middleIndex+1;
        int k=lowerIndex;
        while (i<middleIndex && j<=higherIndex){
            if (tempMergeArr[i] <= tempMergeArr[j]){
                array[k] = tempMergeArr[i];
                i++;
            }else {
                array[k] = tempMergeArr[j];
                j++;
            }
            k++;
        }
        while (i<=middleIndex){
            array[k] = tempMergeArr[i];
            k++;
            i++;
        }
    }
}
