package io.hotely.config.HTTP;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.http.HttpException;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpHandler {

  @CrossOrigin(origins = "192.168.1.107")
  public static HttpResponse<String> rawRequest(String host, int port) throws URISyntaxException, IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://" + host + ":" + port + "/v1/sys/health"))
      .headers("Accept-Encoding", "gzip, deflate")
      .build();
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    return response;
  }

  public static Boolean vaultStatus(String host, int port) throws HttpException {
    try {
      HttpResponse<String> res = rawRequest(host, port);
      int statusCode = res.statusCode();
      return (statusCode == 200) ? true : false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static String simpleStatus(String host) throws HttpException {
    Boolean status = vaultStatus(host, 8200);
    return status ? "UP" : "DOWN";
  }
}
