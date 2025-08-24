package mallang.sallang.riskInfo.controller;

import lombok.RequiredArgsConstructor;
import mallang.sallang.riskInfo.dto.RiskInfoRequestDto;
import mallang.sallang.riskInfo.dto.RiskInfoResponseDto;
import mallang.sallang.riskInfo.service.RiskInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = {"https://sallang.netlify.app", "http://localhost:5173"})
@RequiredArgsConstructor
public class RiskInfoController {

    private final RiskInfoService riskInfoService;

    @PostMapping("/calculate")
    public RiskInfoResponseDto calculateRisk(@RequestBody RiskInfoRequestDto requestDto) {
        // RiskInfoService에서 위험도 계산과 RealtimeRisk 생성 모두 처리
        return riskInfoService.calculateAndCreateResponse(requestDto);
    }
}
