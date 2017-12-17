package be.zlz.zlzbin.bin.tasks;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.repositories.BinRepository;
import be.zlz.zlzbin.bin.repositories.RequestRepository;
import be.zlz.zlzbin.bin.services.BinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BinCleanupTask {

    private Logger logger;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private BinService binService;

    public BinCleanupTask() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanOldBins() {
        logger.info("Running cleanup task...");
        LocalDate lastweek = LocalDate.now().minus(1, ChronoUnit.WEEKS);
        List<Bin> toDelete = binRepository.getBinByLastRequestBeforeAndPermanentIsFalse(Date.from(lastweek.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if(toDelete != null){
            toDelete.forEach(bin -> {
                logger.info("Found bin " + bin.getId() + " with name " + bin.getName() + " and creationdate " + bin.getCreationDate().toString() + ". Deleting...");
                binService.deleteBin(bin);
            });
        }
        else{
            logger.info("No expired bins");
        }

        logger.info("Cleanup completed");
    }
}
