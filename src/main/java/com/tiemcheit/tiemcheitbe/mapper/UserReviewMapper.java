package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.UserReviewRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserReviewResponse;
import com.tiemcheit.tiemcheitbe.model.UserReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserReviewMapper {

    UserReview toUserReview(UserReviewRequest userReviewRequest);

    UserReviewResponse toUserReviewResponse(UserReview userReview);
}
