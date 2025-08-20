package mallang.sallang.restArea.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mallang.sallang.restArea.dto.RestAreaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestAreaService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newHttpClient();

    // type 별로 1회씩 호출해서 합침
    public List<RestAreaDto> find(double lat, double lon, int radiusMeters, List<String> types, int limit) {
        if (types == null || types.isEmpty()) types = List.of("cafe", "convenience_store");

        Map<String, RestAreaDto> merged = new LinkedHashMap<>();
        for (String type : types) {
            callNearby(lat, lon, radiusMeters, type).forEach(p -> merged.putIfAbsent(p.getName() + "@" + p.getLat() + "," + p.getLng(), p));
        }

        // 거리 계산 및 정렬
        List<RestAreaDto> out = merged.values().stream()
                .peek(p -> p.setDistance((int) haversineMeters(lat, lon, p.getLat(), p.getLng())))
                .sorted(Comparator.comparingInt(RestAreaDto::getDistance))
                .limit(limit > 0 ? limit : 30)
                .collect(Collectors.toList());

        // Place Details로 도로명 주소 보강
        for (RestAreaDto p : out) {
            String fa = fetchFormattedAddressKo(p.getPlaceId());
            if (fa != null && !fa.isBlank()) p.setAddress(fa);
        }

        return out;
    }

    private List<RestAreaDto> callNearby(double lat, double lon, int radiusMeters, String type) {
        try {
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&language=ko&key=%s",
                    lat, lon, radiusMeters, type, apiKey
            );

            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(res.body());
            JsonNode results = root.get("results");
            if (results == null || !results.isArray()) return List.of();

            List<RestAreaDto> list = new ArrayList<>();
            for (JsonNode r : results) {
                JsonNode geo = r.path("geometry").path("location");
                double plat = geo.path("lat").asDouble();
                double plng = geo.path("lng").asDouble();

                list.add(RestAreaDto.builder()
                        .placeId(r.get("place_id").asText())
                        .name(r.path("name").asText(""))
                        .address(null) // 간단히 vicinity 사용
                        .lat(plat)
                        .lng(plng)
                        .distance(0)
                        .build());
            }
            return list;
        } catch (Exception e) {
            return List.of();
        }
    }

    // Place Details로 도로명주소 조회
    private String fetchFormattedAddressKo(String placeId) {
        if (placeId == null || placeId.isBlank()) return null;
        try {
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=formatted_address&language=ko&key=%s",
                    placeId, apiKey
            );
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode result = mapper.readTree(res.body()).path("result");
            return result.path("formatted_address").asText(null);
        } catch (Exception e) {
            return null;
        }
    }

    // 직선거리 계산(미터)
    private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
