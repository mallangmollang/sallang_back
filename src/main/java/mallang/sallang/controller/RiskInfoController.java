package mallang.sallang.controller;

import lombok.RequiredArgsConstructor;
import mallang.sallang.dto.riskIfoDto.RiskInfoRequestDto;
import mallang.sallang.dto.riskIfoDto.RiskInfoResponseDto;
import mallang.sallang.service.RiskInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskInfoController {

    private final RiskInfoService riskInfoService;

    @PostMapping("/calculate")
    public RiskInfoResponseDto calculateRisk(@RequestBody RiskInfoRequestDto requestDto) {
        // RiskInfoService에서 위험도 계산과 RealtimeRisk 생성 모두 처리
        return riskInfoService.calculateAndCreateResponse(requestDto);
    }
}
