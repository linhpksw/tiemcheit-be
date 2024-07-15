package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.FeedbackRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.FeedbackResponse;
import com.tiemcheit.tiemcheitbe.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("")
//    public ApiResponse<List<FeedbackResponse>> allFeedbacks() {
//        var data = feedbackService.allFeedbacks();
//        return ApiResponse.<List<FeedbackResponse>>builder()
//                .message("Success")
//                .data(data).build();
//    }
    public ApiResponse<List<FeedbackResponse>> allFeedbacks(
            @RequestParam String startDate,
            @RequestParam String endDate) throws ParseException {
        Date formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedEndDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date exactEndDate = calendar.getTime();
        var data = feedbackService.allFeedbacksInTimeRange(formattedStartDate, exactEndDate);
        return ApiResponse.<List<FeedbackResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @PostMapping("")
    public ApiResponse<FeedbackResponse> sendFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        var data = feedbackService.sendFeedback(feedbackRequest);
        return ApiResponse.<FeedbackResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @PatchMapping("single")
    public ApiResponse<FeedbackResponse> updateFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        var data = feedbackService.updateFeedback(feedbackRequest);
        return ApiResponse.<FeedbackResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @PatchMapping("multi")
    public ApiResponse<List<FeedbackResponse>> updateFeedbacks(@RequestBody List<FeedbackRequest> feedbackRequests) {
        var data = feedbackService.updateFeedbacks(feedbackRequests);
        return ApiResponse.<List<FeedbackResponse>>builder()
                .message("Success")
                .data(data).build();
    }
}
