package mallang.sallang.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mallang.sallang.report.dto.ReportRequestDto;
import mallang.sallang.report.dto.ReportResponseDto;
import mallang.sallang.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "https://sunny-boba-b65221.netlify.app")
@Tag(name = "일일 레포트", description = "일일 레포트 생성 컨트롤러")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @Operation(summary = "일일 레포트 생성", description = "프론트엔드에서 넘겨받은 정보로 AI 기반 일일 레포트를 생성")
    public ResponseEntity<ReportResponseDto> createDailyReport(@RequestBody ReportRequestDto requestDto) {
        ReportResponseDto responseDto = reportService.createDailyReport(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
