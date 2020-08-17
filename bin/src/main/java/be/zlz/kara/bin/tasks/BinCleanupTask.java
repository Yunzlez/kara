package be.zlz.kara.bin.tasks;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.enums.BinConfigKey;
import be.zlz.kara.bin.repositories.BinRepository;
import be.zlz.kara.bin.services.BinService;
import be.zlz.kara.bin.util.SizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final Logger logger;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private BinService binService;

    @Value("${bin.max.size.permanent.bytes:50000000}")
    private long maxPermanentBinSize;

    @Value("${bin.max.size.bytes:100000000}")
    private long maxBinSize;

    @Value("${bin.compaction.leave.count:100}")
    private int compactTo;

    public BinCleanupTask() {
        logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Scheduled(fixedDelay = 3600000, initialDelay = 30000)
    public void cleanOldBins() {
        logger.info("Running cleanup task...");
        deleteExpiredBins();
        compactBins();
        logger.info("Cleanup completed");
    }

    private void deleteExpiredBins(){
        LocalDate lastweek = LocalDate.now().minus(1, ChronoUnit.WEEKS);
        List<Bin> oldBins = binRepository.getBinByLastRequestBefore(Date.from(lastweek.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if(oldBins != null){
            oldBins.stream()
                    .filter(b -> !b.isEnabled(BinConfigKey.PERMANENT_KEY)) //todo move back to query to filter out these
                    .forEach(bin -> {
                        logger.info("Found expired bin " + bin.getId() + " with name " + bin.getName() + " and creationdate " + bin.getCreationDate().toString() + ". Deleting...");
                        binService.deleteBin(bin);
                    });
        }
        else{
            logger.info("No expired bins");
        }
    }

    private void compactBins(){
        Iterable<Bin> bins = binRepository.findAll();

        for (Bin current : bins) {
            Long size = binRepository.getBinSizeInBytes(current.getId());
            size = size == null ? 0 : size;
            logger.info("Bin {} is at {}", current.getName(), SizeUtil.autoScale(size));
            if (current.isPermanent()) {
                compactIfExceeds(current, size, maxPermanentBinSize);
            } else {
                compactIfExceeds(current, size, maxBinSize);
            }
        }
    }

    private void compactIfExceeds(Bin bin, long binSize, long maxSize) {
        if(binSize < maxSize){
            logger.info("Bin {} does not exceed max size", bin.getName());
            return;
        }
        logger.info("Compacting bin {} as it exceeds max size of {}", bin.getName(), SizeUtil.autoScale(maxSize));
        binService.compactBin(bin, compactTo);
    }

}
