package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.FeedbackRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.FeedbackResponse;
import com.tiemcheit.tiemcheitbe.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("")
    public ApiResponse<List<FeedbackResponse>> allFeedbacks() {
        var data = feedbackService.allFeedbacks();
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
