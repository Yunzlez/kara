package be.zlz.zlzbin.bin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
public class DelayService {

    @Value("${max.ms.delay}")
    private int maxMs;

    @Autowired
    private Semaphore delaySemaphore;

    public boolean delay(int ms){
        int realDelay = Math.abs(ms);
        if(realDelay > maxMs){
            throw new IllegalArgumentException("Max delay is " + ms + "ms");
        }
        try {
            delaySemaphore.tryAcquire(50, TimeUnit.MILLISECONDS);
            Thread.sleep(realDelay);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
