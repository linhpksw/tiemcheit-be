package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.FeedbackRequest;
import com.tiemcheit.tiemcheitbe.dto.response.FeedbackResponse;
import com.tiemcheit.tiemcheitbe.mapper.FeedbackMapper;
import com.tiemcheit.tiemcheitbe.model.Feedback;
import com.tiemcheit.tiemcheitbe.repository.FeedbackRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepo feedbackRepo;
    private final FeedbackMapper feedbackMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<FeedbackResponse> allFeedbacks() {
        List<Feedback> feedbacks = feedbackRepo.findAll();
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();

        for (Feedback f : feedbacks) {
            feedbackResponses.add(feedbackMapper.toFeedbackResponse(f));
        }

        return feedbackResponses;
    }

    public FeedbackResponse sendFeedback(FeedbackRequest feedbackRequest) {
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        Feedback savedFeedback = feedbackRepo.save(feedback);
        return feedbackMapper.toFeedbackResponse(savedFeedback);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FeedbackResponse updateFeedback(FeedbackRequest feedbackRequest) {
        Feedback feedbackToUpdate = feedbackRepo.getReferenceById(feedbackRequest.getId());
        feedbackToUpdate.setRead(feedbackRequest.isRead());
        Feedback updatedFeedback = feedbackRepo.save(feedbackToUpdate);
        return feedbackMapper.toFeedbackResponse(updatedFeedback);
    }
}
