package be.zlz.zlzbin.bin.services;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.repositories.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HarService {

    @Autowired
    private BinRepository binRepository;

    public Object getHarFor(String bin){
        return getHarFor(binRepository.getByName(bin));
    }
    public Object getHarFor(Bin bin){
        if(bin == null){
            //todo error
        }
        return null;
    }
}
