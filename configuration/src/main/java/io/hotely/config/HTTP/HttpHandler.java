package io.hotely.config.HTTP;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpHandler {

  public static HttpResponse<String> rawRequest() throws URISyntaxException, IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://127.0.0.1:8200/v1/sys/health"))
      .headers("Accept-Encoding", "gzip, deflate")
      .build();
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    return response;
  }

  public static Boolean vaultStatus() throws Exception {
    try {
      HttpResponse<String> res = rawRequest();
      int statusCode = res.statusCode();
      return (statusCode == 200) ? true : false;
    } catch (Exception e) {
      return false;
    }
  }

  public static String simpleStatus() throws Exception {
    Boolean status = vaultStatus();
    return status ? "UP" : "DOWN";
  }
}
