package domaci;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Asistent implements Runnable{
    Semaphore asistent;
    volatile boolean gasi=false;
    Random random=new Random();

    public void stani(){
        gasi=true;

    }

    public Asistent(Semaphore asistent) {
        this.asistent = asistent;
    }

    @Override
    public void run() {
        App.asist=true;

        try {
            asistent.acquire();
            while(!gasi){

                    for (int i = 0; i < App.ocene.length(); i++) {
                        if (App.ocene.get(i) == -1) {
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
            asistent.release();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
