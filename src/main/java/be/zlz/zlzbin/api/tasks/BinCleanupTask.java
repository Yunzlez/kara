package be.zlz.zlzbin.api.tasks;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class BinCleanupTask {

    private Logger logger;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private RequestRepository requestRepository;

    public BinCleanupTask() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanOldBins() {
        logger.info("Running cleanup task...");
        LocalDate lastweek = LocalDate.now().minus(1, ChronoUnit.WEEKS);
        List<Bin> toDelete = binRepository.getBinByCreationDateBefore(Date.from(lastweek.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        toDelete.forEach(bin -> {
            long id = bin.getId();
            logger.debug("Found bin " + id + " with name " + bin.getName() + " and creationdate " + bin.getCreationDate().toString());
            requestRepository.deleteAllByBin(bin);
            binRepository.delete(bin);
        });
        logger.info("Cleanup completed");
    }
}
