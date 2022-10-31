package com.example.authmoduls;

public class Pattern {
    public static void main(String[] args) {
        double[] x = {15,12,91,55.44,66.1,99,41,46};

        double sum = 0;
        for (int i=0;i<x.length;i++ ){
            sum+=x[i];
        }
        System.out.println("Total is: "+sum);

        double max=x[0];
        for (int i=0;i<x.length;i++){
            if(x[i]>max) max = x[i];
        }
        System.out.println("Max number is: "+max);

        double min=x[0];
        for (int i=0;i<x.length;i++){
            if (x[i]<min) min = x[i];
        }
        System.out.println("Min number is: "+min);

    }
    }
