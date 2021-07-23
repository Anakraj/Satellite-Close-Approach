package com.example.orbittracker;

//simple multithreading takes 2 ms
//no multithreading takes 38 ms
public class MultithreadingExample {

    public static void main(String[] args){
        final long startTime = System.currentTimeMillis();

        //number of threads
        int n = 10;

        //each thread
        for (int i = 0; i < n; i++) {
            MultithreadingDemo object = new MultithreadingDemo();
            object.start();
        }


        //example of no multithreading
        for(int i = 0; i < n; i ++){
            System.out.println("Thread " + (11 + i) + " is running");
        }


        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));

    }

    }

class MultithreadingDemo extends Thread {
    public void run(){
        try{
            System.out.println("Thread " + Thread.currentThread().getId() + " is running");
        }catch(Exception e){
            System.out.println("Exception is caught");
        }
    }
}
