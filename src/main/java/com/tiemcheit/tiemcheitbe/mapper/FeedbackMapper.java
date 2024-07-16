package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.FeedbackRequest;
import com.tiemcheit.tiemcheitbe.dto.response.FeedbackResponse;
import com.tiemcheit.tiemcheitbe.model.Feedback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback toFeedback(FeedbackRequest request);

    FeedbackResponse toFeedbackResponse(Feedback feedback);
}
