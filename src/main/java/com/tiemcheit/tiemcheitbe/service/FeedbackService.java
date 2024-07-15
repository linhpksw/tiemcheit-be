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
import java.util.Collections;
import java.util.Date;
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
        Collections.reverse(feedbackResponses);

        return feedbackResponses;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<FeedbackResponse> allFeedbacksInTimeRange(Date startDate, Date endDate) {
        List<Feedback> feedbacks = feedbackRepo.findAllByDateRange(startDate, endDate);
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();

        for (Feedback f : feedbacks) {
            feedbackResponses.add(feedbackMapper.toFeedbackResponse(f));
        }
        Collections.reverse(feedbackResponses);

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<FeedbackResponse> updateFeedbacks(List<FeedbackRequest> feedbackRequest) {
        List<FeedbackResponse> responses = new ArrayList<>();
        for (FeedbackRequest req : feedbackRequest) {
            Feedback feedbackToUpdate = feedbackRepo.getReferenceById(req.getId());
            feedbackToUpdate.setRead(req.isRead());
            Feedback updatedFeedback = feedbackRepo.save(feedbackToUpdate);
            responses.add(feedbackMapper.toFeedbackResponse(updatedFeedback));
        }
        return responses;
    }
}
