import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Parsing {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void getCat(CloseableHttpResponse response) {

        // вывод полученных заголовков
//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        // чтение тела ответа
        String body = null;
        try {
            body = new String(response.getEntity()
                    .getContent()
                    .readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(body);

        // получим статус запроса
//        System.out.println(response.getStatusLine());

        // код для преобразования json в java
        List<Post> posts = null;
        try {
            posts = mapper.readValue(body, new TypeReference<>() {
            });
//            posts.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // фильтруем нужные значения
        List<Post> filterPost =
                posts != null ? posts.stream()
                        .filter(upvotes -> upvotes.getUpvotes() > 0)
                        .collect(Collectors.toList()) : null;
//        if (filterPost != null) {
//            filterPost.forEach(System.out::println);
//        }

//        // переведём List в строку
        String resultJson = null;
        try {
            resultJson = mapper.writeValueAsString(filterPost);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

//        // преобразуем строку в 'красивый' json
        Object json = null;
        try {
            json = mapper.readValue(resultJson, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
