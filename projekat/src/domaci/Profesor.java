package domaci;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Profesor implements Runnable{


    Semaphore profesor;
    volatile boolean gasi=false;
    Random random=new Random();
    CyclicBarrier pomprof;

    public void stani(){
        gasi=true;

    }
    public void greskaBarijera() throws BrokenBarrierException, InterruptedException {
        if(pomprof.getNumberWaiting()==1) pomprof.await();
    }

    public Profesor(Semaphore profesor, CyclicBarrier pomprof) {

        this.profesor = profesor;
        this.pomprof=pomprof;
    }

    @Override
    public void run() {
        App.prof=true;

        try {
            profesor.acquire();
            while(!gasi){

                for (int i = 0; i < App.ocene.length(); i++) {
                    if (App.ocene.get(i) == -2) {
                        int ispitivanje = random.nextInt((1000 - 500) + 1) + 500;
                        Thread.sleep(ispitivanje);
                        App.vremeIspitivanja.set(i,ispitivanje);
                        int ocena = random.nextInt((10 - 5) + 1) + 5;
                        if(!App.beskonacnaZaStudenta){
                            App.ocene.set(i,-10);
                        }
                        else{
                           // System.out.println("vreme ispitivanja studenta " + i + " je " + ispitivanje);
                            App.ocene.set(i, ocena);
                        }


                    }

                }


            }
            profesor.release();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
