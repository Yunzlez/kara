package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Request;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class SolrService {

    private static final Logger LOG = LoggerFactory.getLogger(SolrService.class);

    //@Value("${solr.endpoint}")
    private String solrEndpoint = "http://localhost:8983/solr/karaa";

    private SolrClient solrClient;

    public SolrService(){
        solrClient = new HttpSolrClient.Builder(solrEndpoint).build();
    }

    public void add(Request request){
        checkNotNull(request);
        SolrInputDocument document = getSolrDocument(request);

        SolrClient solrClient = new HttpSolrClient.Builder(solrEndpoint).build();
        try {
            solrClient.add(document);
            solrClient.commit();
            solrClient.close();
        } catch (SolrServerException | IOException e) {
            LOG.error("failed to push to Solr: ", e);
        }
    }

    public List<Integer> query(String query){
        List<Integer> ids = new ArrayList<>();

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);

        QueryRequest request = new QueryRequest(solrQuery);

        try {
            QueryResponse response = request.process(solrClient);

            response.getResults().forEach(doc -> ids.add(Integer.parseInt(doc.getFieldValue("id").toString())));
        } catch (SolrServerException | IOException e) {
            LOG.error("failed to process Solr query: ", e);
        }
        return ids;
    }

    public void remove(Request request){
        checkNotNull(request);
        try {
            solrClient.deleteById(String.valueOf(request.getId()));
        } catch (SolrServerException | IOException e) {
            LOG.error("failed to delete: ", e);
        }
    }

    public void removeForBin(Bin bin){
        checkNotNull(bin);
        try {
            solrClient.deleteByQuery("bin:"+bin.getName());
        } catch (SolrServerException | IOException e) {
            LOG.error("could not delete requests for " + bin.getName());
        }
    }


    private SolrInputDocument getSolrDocument(Request request) {
        SolrInputDocument document = new SolrInputDocument();

        document.addField("id", request.getId());
        document.addField("method", request.getMethod().toString());
        document.addField("requestTime", request.getRequestTime().getTime());
        document.addField("body", request.getBody());
        document.addField("headers", "");
        document.addField("protocol", request.getProtocol());
        document.addField("queryParams", "");
        document.addField("bin", request.getBin().getName());
        return document;
    }
}
