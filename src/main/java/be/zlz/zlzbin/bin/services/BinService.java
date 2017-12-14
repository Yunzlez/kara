package be.zlz.zlzbin.bin.services;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.repositories.BinRepository;
import be.zlz.zlzbin.bin.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BinService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private BinRepository binRepository;


    public void deleteBin(Bin bin){
        long id = bin.getId();
        requestRepository.deleteAllByBin(bin);
        binRepository.delete(bin);
    }

    @Transactional
    public void clearBin(String uuid){
        Bin bin = binRepository.getByName(uuid);
        if(bin != null){
            bin.setRequestCount(0);
            bin.getRequestMetric().getCounts().clear();
            requestRepository.deleteAllByBin(bin);
            binRepository.save(bin);
        }
    }
}
