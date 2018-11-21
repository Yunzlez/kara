package be.zlz.kara.bin.tasks;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.BinConfigKey;
import be.zlz.kara.bin.repositories.BinRepository;
import be.zlz.kara.bin.repositories.RequestRepository;
import be.zlz.kara.bin.services.BinService;
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
        List<Bin> oldBins = binRepository.getBinByLastRequestBefore(Date.from(lastweek.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if(oldBins != null){
            oldBins.stream()
                    .filter(b -> !b.isEnabled(BinConfigKey.PERMANENT_KEY)) //todo move back to query to filter out these
                    .forEach(bin -> {
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
