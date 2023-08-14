package domaci;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Student implements Runnable {

   static volatile AtomicInteger zaProfu=new AtomicInteger(0);
    Semaphore asistent;
    Semaphore profesor;
    CyclicBarrier pomprof;
    long pocetak;
    Random random=new Random();
    int redniBroj;
    int ocena;
    int vremeDolaska;


    public Student(CyclicBarrier pomprof,Semaphore profesor,Semaphore asistent, int redniBroj) {
        this.asistent = asistent;
        this.redniBroj = redniBroj;
        this.profesor=profesor;
        this.pomprof=pomprof;
    }


    @Override
    public void run() {


        try {
            vremeDolaska = random.nextInt((900 - 1) + 1) + 1;
            Thread.sleep(vremeDolaska);
           // System.out.println("vreme dolaska studenta " + redniBroj + " je " + vremeDolaska + " vreme pravo " + (System.currentTimeMillis() - App.vreme));

            if (vremeDolaska % 2== 0) {
                while (!asistent.tryAcquire()) {
                }
                pocetak = System.currentTimeMillis();
                if (App.beskonacnaZaStudenta) {
                   // System.out.println("Redni broj " + redniBroj + " je zapoceo odbranu kod asistenta u " + pocetak);
                    App.ocene.set(redniBroj, -1);
                    //System.out.println("Redni broj ucenika je: " + redniBroj + " i on je setovao svoju ocenu na -1");
                }
                while (App.ocene.get(redniBroj) == -1 && App.beskonacnaZaStudenta) {
                }
                ocena = App.ocene.get(redniBroj);
                if (App.beskonacnaZaStudenta)
                    //System.out.println("Redni broj ucenika je: " + redniBroj + " i njegova ocena je " + ocena);
                    System.out.println("Thread: "+ redniBroj +
                            "\nArrival: "+vremeDolaska +
                            "\nProf: asistent \n" +
                            "TTC: "+App.vremeIspitivanja.get(redniBroj)+":"+pocetak +
                            "\nScore: "+App.ocene.get(redniBroj));
                System.out.println("------------------------------------------");
                if (App.ocene.get(redniBroj) == -1 && !App.beskonacnaZaStudenta) {
                   // System.out.println("Redni broj prekinutog ucenika je: " + redniBroj + " i njegova ocena je -10");
                    //System.out.println(System.currentTimeMillis() - App.vreme + " " + (System.currentTimeMillis() - pocetak));
                    System.out.println("PREKINUT STUDENT!! Thread: "+ redniBroj +
                            "\nArrival: "+vremeDolaska +
                            "\nProf: asistent \n" +
                            "TTC: "+(System.currentTimeMillis() - pocetak)+":"+pocetak +
                            "\nScore: -10");
                    System.out.println("------------------------------------------");
                }
                asistent.release();
            } else {
                pomprof.await();
                if (App.greskaKodBarijere){
                    while (!profesor.tryAcquire()) {
                    }
                pocetak = System.currentTimeMillis();
                if (App.beskonacnaZaStudenta) {
                   // System.out.println("Redni broj " + redniBroj + " je zapoceo odbranu kod profe u " + pocetak);
                    App.ocene.set(redniBroj, -2);
                   // System.out.println("Redni broj ucenika je: " + redniBroj + " i on je setovao svoju ocenu na -2");
                }
                while ((App.ocene.get(redniBroj) == -2 && App.beskonacnaZaStudenta)) {
                }
                ocena = App.ocene.get(redniBroj);
                if (App.beskonacnaZaStudenta) {
                    //System.out.println("Redni broj ucenika je: " + redniBroj + " i njegova ocena je " + ocena);
                    System.out.println("Thread: "+ redniBroj +
                            "\nArrival: "+vremeDolaska +
                            "\nProf: profesor \n" +
                            "TTC: "+App.vremeIspitivanja.get(redniBroj)+":"+pocetak +
                            "\nScore: "+App.ocene.get(redniBroj));
                    System.out.println("------------------------------------------");
                }
                if (App.ocene.get(redniBroj) == -2 && !App.beskonacnaZaStudenta) {
                    //System.out.println("Redni broj prekinutog ucenika je: " + redniBroj + " i njegova ocena je -10");
                  //  System.out.println(System.currentTimeMillis() - App.vreme + " " + (System.currentTimeMillis() - pocetak));
                    System.out.println("PREKINUT STUDENT!! Thread: "+ redniBroj +
                            "\nArrival: "+vremeDolaska +
                            "\nProf: profesor \n" +
                            "TTC: "+(System.currentTimeMillis() - pocetak)+":"+pocetak +
                            "\nScore: -10");
                    System.out.println("------------------------------------------");
                }
                zaProfu.getAndIncrement();
                if(zaProfu.get()==2)
                profesor.release(2);
                if(!App.beskonacnaZaStudenta)
                    profesor.release(2);

            }
        }


        } catch (InterruptedException e) {
            System.out.println("student broj "+redniBroj+" je prekinut");
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }



    }
}
