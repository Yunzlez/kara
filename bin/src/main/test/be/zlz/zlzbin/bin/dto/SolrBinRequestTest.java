package be.zlz.zlzbin.bin.dto;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.domain.Request;
import be.zlz.zlzbin.bin.services.SolrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class SolrBinRequestTest {

    @Test
    public void run(){
        Request request = new Request(HttpMethod.GET, "body", "http1", new HashMap<>());
        Bin b = new Bin();
        b.setName("bin");
        request.setBin(b);

        SolrService solrService = new SolrService();
        solrService.add(request);
    }

    @Test
    public void query(){
        SolrService solrService = new SolrService();
        List<Integer> ids = solrService.query("*:*");
        System.out.println(ids);
    }
}
