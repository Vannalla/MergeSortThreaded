import java.util.Random;
import java.util.Scanner;

public class MergeSortThreaded {
    static int[] result; 
    public static void finalMerge(int[] a, int[] b) {
        //int[] result = new int[a.length + b.length];
        result = new int[a.length + b.length];
        int i=0; 
        int j=0; 
        int r=0;
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                result[r]=a[i];
                i++;
                r++;
            } else {
                result[r]=b[j];
                j++;
                r++;
            }
            if (i==a.length) {
                while (j<b.length) {
                    result[r]=b[j];
                    r++;
                    j++;
                }
            }
            if (j==b.length) {
                while (i<a.length) {
                    result[r]=a[i];
                    r++;
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Testing threaded quicksort.");
        System.out.println("Please enter size of the array you would like to sort, Note that the code wont print arrays bigger than 100, but it does sort! ");
        Scanner scan = new Scanner(System.in);
        int size = scan.nextInt();
        Random rand = new Random();
        int[] original = new int[size];
        for (int i=0; i<original.length; i++) {
            original[i] = rand.nextInt(100);
        }
        if(original.length< 100){
            System.out.print("Unsorted array: ");
            for (int i = 0; i < original.length; i++) System.out.print(original[i] + " ");
            System.out.print("\n");
        }

        int[] subArr1 = new int[original.length/2];
        int[] subArr2 = new int[original.length - original.length/2];
        System.arraycopy(original, 0, subArr1, 0, original.length/2);
        System.arraycopy(original, original.length/2, subArr2, 0, original.length - original.length/2);
        Worker runner1 = new Worker(subArr1);
        Worker runner2 = new Worker(subArr2);
        runner1.start();
        runner2.start();
        runner1.join();
        runner2.join();
        finalMerge (runner1.getInternal(), runner2.getInternal());
        if(result.length< 100){
            System.out.print("Sorted array: ");
        for (int i = 0; i < result.length; i++) System.out.print(result[i] + " ");
            System.out.print("\n");
        }
        System.out.println("All Done!");
        scan.close();
    }

}

class Worker extends Thread {
    private int[] internal;

    public int[] getInternal() {
        return internal;
    }

    public void mergeSort(int[] array) {
        if (array.length > 1) {
            int[] left = leftHalf(array);
            int[] right = rightHalf(array);

            mergeSort(left);
            mergeSort(right);

            merge(array, left, right);
        }
    }

    public int[] leftHalf(int[] array) {
        int size1 = array.length / 2;
        int[] left = new int[size1];
        for (int i = 0; i < size1; i++) {
            left[i] = array[i];
        }
        return left;
    }

    public int[] rightHalf(int[] array) {
        int size1 = array.length / 2;
        int size2 = array.length - size1;
        int[] right = new int[size2];
        for (int i = 0; i < size2; i++) {
            right[i] = array[i + size1];
        }
        return right;
    }

    public void merge(int[] result, int[] left, int[] right) {
        int i1 = 0;   
        int i2 = 0;   

        for (int i = 0; i < result.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1] <= right[i2])) {
                result[i] = left[i1];   
                i1++;
            } else {
                result[i] = right[i2];   
                i2++;
            }
        }
    }
    Worker(int[] arr) {
        internal = arr;
    }

    public void run() {
        mergeSort(internal);
    }
}