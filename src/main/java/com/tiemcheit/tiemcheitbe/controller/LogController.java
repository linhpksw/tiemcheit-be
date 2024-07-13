package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.LogRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.LogResponse;
import com.tiemcheit.tiemcheitbe.service.LogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogController {
    private final LogService logService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Sorting logs by updated time in descending order
        Pageable sortedByUpdatedDesc = PageRequest.of(page, size, Sort.by(direction, "timestamp"));

        Page<LogResponse> logs = logService.findAllLogs(sortedByUpdatedDesc);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("logs", logs.getContent());
        responseData.put("totalPages", logs.getTotalPages());

        return ApiResponse.<Map<String, Object>>builder()
                .data(responseData)
                .message("Success")
                .build();
    }

    @PostMapping
    public ApiResponse<LogResponse> createLog(@RequestBody @Valid LogRequest logRequest) {
        LogResponse log = logService.createLog(logRequest);
        return ApiResponse.<LogResponse>builder()
                .data(log)
                .message("Success")
                .build();
    }
}
