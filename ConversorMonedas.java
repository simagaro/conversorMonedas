import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Scanner;

public class ConversorMonedas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce la moneda de origen (por ejemplo, USD): ");
        String monedaOrigen = scanner.nextLine().toUpperCase();


        System.out.println("Introduce la cantidad de " + monedaOrigen + " que deseas convertir: ");
        double cantidad = scanner.nextDouble();


        String monedaDestino = "COP";


        String apiKey = "00cacdd03141b8c55694f0ff";
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + monedaOrigen;


        convertirMoneda(url, monedaDestino, cantidad, monedaOrigen);


        scanner.close();
    }

    public static void convertirMoneda(String url, String monedaDestino, double cantidad, String monedaOrigen) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");

            if (conversionRates.has(monedaDestino)) {
                double tasaCambio = conversionRates.get(monedaDestino).getAsDouble();
                double resultado = cantidad * tasaCambio;

                System.out.printf("%.2f %s es igual a %.2f %s.%n", cantidad, monedaOrigen, resultado, monedaDestino);
            } else {
                System.out.println("Error: No se encontró la tasa de cambio para " + monedaDestino);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: No se pudo realizar la conversión.");
            e.printStackTrace();
        }
    }
}
