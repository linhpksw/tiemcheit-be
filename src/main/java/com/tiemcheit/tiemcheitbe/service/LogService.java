package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.LogRequest;
import com.tiemcheit.tiemcheitbe.dto.response.LogResponse;
import com.tiemcheit.tiemcheitbe.mapper.LogMapper;
import com.tiemcheit.tiemcheitbe.model.Log;
import com.tiemcheit.tiemcheitbe.repository.LogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final LogRepo logRepo;
    private final LogMapper logMapper;

//    public Page<LogResponse> findAllLogs(Pageable pageable) {
//        return logRepo.findAll(pageable).map(logMapper::toLogResponse);
//    }
//
//    public Page<LogResponse> findLogsByStatus(Pageable pageable, Integer status) {
//        return logRepo.findByResponseStatus(pageable, status).map(logMapper::toLogResponse);
//    }
//
//    public Page<LogResponse> findLogsByStatusNot(Pageable pageable, Integer status) {
//        return logRepo.findByResponseStatusNot(pageable, status).map(logMapper::toLogResponse);
//    }

    public Page<LogResponse> findLogsByDateRange(Pageable pageable, LocalDate startDate, LocalDate endDate, String status) {
        if (status.equals("all")) {
            return logRepo.findAllByDateRange(pageable, startDate, endDate).map(logMapper::toLogResponse);
        } else if (status.equals("200")) {
            return logRepo.findByDateRangeAndStatus(pageable, startDate, endDate, 200).map(logMapper::toLogResponse);
        } else {
            return logRepo.findByDateRangeAndStatusNot(pageable, startDate, endDate, 200).map(logMapper::toLogResponse);
        }
    }

    public LogResponse createLog(LogRequest request) {
        Log logEntry = Log.builder()
                .timestamp(request.getTimestamp())
                .username(request.getUsername())
                .apiEndpoint(request.getApiEndpoint())
                .requestMethod(request.getRequestMethod())
                .responseStatus(request.getResponseStatus())
                .message(request.getMessage())
                .executionTime(request.getExecutionTime())
                .userAgent(request.getUserAgent())
                .build();

        Log savedEntry = logRepo.save(logEntry);
        return logMapper.toLogResponse(savedEntry);
    }
}
