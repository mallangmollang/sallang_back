package mallang.sallang.restArea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mallang.sallang.restArea.dto.RestAreaDto;
import mallang.sallang.restArea.service.RestAreaService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/restarea")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "쉼터 추천", description = "위치 기반 쉼터 추천 서비스")
public class RestAreaController {
    private final RestAreaService restAreaService;

    public RestAreaController(RestAreaService restAreaService) {
        this.restAreaService = restAreaService;
    }

    @GetMapping
    @Operation(summary = "쉼터 검색 후 리스트 반환", description = "파라미터로 넘겨받은 위치정보 기반 주변 카페, 편의점 검색")
    public List<RestAreaDto> getAreaList(@RequestParam double lat,
                                         @RequestParam double lon,
                                         @RequestParam(defaultValue = "2000") int radius,
                                         @RequestParam(required = false) String types,
                                         @RequestParam(defaultValue = "5") int limit) {
        List<String> typeList = (types == null || types.isBlank())
                ? List.of("cafe", "convenience_store")
                : Arrays.stream(types.split(",")).map(String::trim).toList();

        return restAreaService.find(lat, lon, radius, typeList, limit);
    }
}
