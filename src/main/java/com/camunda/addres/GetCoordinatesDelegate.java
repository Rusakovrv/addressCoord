package com.camunda.addres;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class GetCoordinatesDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String strAddr = (String) delegateExecution.getVariable("address");
        HttpClient client = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8081).setPath("/addressservice")
                .setParameter("address", strAddr);
        URI uri = builder.build();
        HttpGet request = new HttpGet(uri);
        request.addHeader("Content-Type", "Content-Type: text/plain");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String responseBody=result.toString();

        delegateExecution.setVariable("coordinates",result.toString());

    }
}
