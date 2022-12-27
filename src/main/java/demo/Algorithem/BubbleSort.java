package demo.Algorithem;

public class BubbleSort {

    public static void main(String[] args) {
       /* int[] a={36,25,40,12,5}; */
        String[] a ={"savan" , "Kiritbhai" , "patel"};

        /*int temp;*/
        String temp;
        //loop for number of count
        for (int i=0;i<a.length;i++){
            int flag = 0;
            //loop for compare elements
            for (int j=0;j<a.length-1-i;j++){
                /*if (a[j]>a[j+1]){*/
                    if (a[j].compareTo(a[j+1])>0){
                    temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                    flag=1;
                }
            }
            if (flag==0){
                break;
            }
        }
        for (int i=0;i<a.length;i++){
            System.out.println(a[i]+"");
        }

    }

}
