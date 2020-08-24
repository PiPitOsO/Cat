import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static final String http = "https://cat-fact.herokuapp.com/facts";

    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet(http);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            ObjectMapper mapper = new ObjectMapper();
            InputStream content = response.getEntity().getContent();
            String data = mapper.readTree(content).get("all").toString();
            List<Cat> cats = mapper.readValue(data, new TypeReference<List<Cat>>() {
            });

            cats.stream().filter(cat -> cat.getUpvotes() != 0).forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}