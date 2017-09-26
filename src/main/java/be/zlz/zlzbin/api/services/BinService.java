package be.zlz.zlzbin.api.services;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
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
            requestRepository.deleteAllByBin(bin);
        }
    }
}
