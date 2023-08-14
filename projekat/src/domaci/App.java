package domaci;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;


public class App{

    public static AtomicIntegerArray ocene;
    public static AtomicIntegerArray vremeIspitivanja;


    public static volatile boolean beskonacnaZaStudenta=true;
    public static volatile boolean greskaKodBarijere=true;
    public static volatile boolean asist=false;
    public static boolean prof=false;
    public static long vreme;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {


        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        ExecutorService executorService= Executors.newFixedThreadPool(n);
        CyclicBarrier pomprof=new CyclicBarrier(2);
        ocene=new AtomicIntegerArray(n);
        vremeIspitivanja=new AtomicIntegerArray(n);
        Semaphore asistent = new Semaphore(2,true);
        Semaphore profesor = new Semaphore(3,true);
        Asistent runnable=new Asistent(asistent);
        Thread asistentTred=new Thread(runnable);
        Profesor runnable2=new Profesor(profesor,pomprof);
        Thread profesorTred=new Thread(runnable2);
        asistentTred.start();
        profesorTred.start();
        while(!asist && !prof){

        }
         vreme=System.currentTimeMillis();
        System.out.println("Vreme pocetka odbrane je "+vreme);
        for(int i=0;i<n;i++){
                Thread student=new Thread(new Student(pomprof,profesor,asistent, i));
                executorService.execute(student);
        }



        Thread.sleep(5000);
        beskonacnaZaStudenta=false;
        //executorService.shutdownNow();
        runnable.stani();
        runnable2.stani();
        greskaKodBarijere=false;
        runnable2.greskaBarijera();
        executorService.shutdownNow();
        double zbir=0;
        int broj=n;
        for(int i=0;i<n;i++){
            if(ocene.get(i)>0)
            zbir+=ocene.get(i)*1.0;
        }
        System.out.println("Zbir svih ocena je "+(zbir/(broj*1.0)));




    }
}
