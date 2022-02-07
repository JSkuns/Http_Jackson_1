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

        // чтение тела ответа
        String body = null;
        try {
            body = new String(response.getEntity()
                    .getContent()
                    .readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // код для преобразования json в java
        List<Post> posts = null;
        try {
            posts = mapper.readValue(body, new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // фильтруем нужные значения
        List<Post> filterPost =
                posts != null ? posts.stream()
                        .filter(upvotes -> upvotes.getUpvotes() != null)
                        .filter(upvotes -> upvotes.getUpvotes() > 0)
                        .collect(Collectors.toList()) : null;

        // вывод на экран
        try {
            String resultJson;
            Object json = null;
            if (filterPost != null) {
                // преобразуем List в строку
                resultJson = mapper.writeValueAsString(filterPost);
                // преобразуем строку в json
                json = mapper.readValue(resultJson, Object.class);
            }
            // сделаем 'красивый' вывод на экран
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
