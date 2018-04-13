package be.zlz.zlzbin.bin.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(DelayService.class);

    public boolean delay(int ms){
        int realDelay = Math.abs(ms);
        if(realDelay > maxMs){
            throw new IllegalArgumentException("Max delay is " + maxMs + "ms");
        }
        try {
            LOG.debug("Delaying for {}ms", ms);
            delaySemaphore.tryAcquire(50, TimeUnit.MILLISECONDS);
            LOG.debug("Semaphore is at {} permits", delaySemaphore.availablePermits());
            Thread.sleep(realDelay);
            return true;
        } catch (InterruptedException e) {
            return false;
        } finally {
            LOG.debug("Releasing semaphore!");
            delaySemaphore.release();
        }
    }
}
