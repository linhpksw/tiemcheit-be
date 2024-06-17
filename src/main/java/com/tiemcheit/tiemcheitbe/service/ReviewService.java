package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.UserReviewRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserReviewResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.mapper.UserReviewMapper;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.model.UserReview;
import com.tiemcheit.tiemcheitbe.repository.OrderDetailRepo;
import com.tiemcheit.tiemcheitbe.repository.ReviewRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final UserRepo userRepo;
    private final UserReviewMapper userReviewMapper;

    public List<UserReviewResponse> getReviewsOfProduct(long productId) {
        List<OrderDetail> orderDetailsOfProduct = orderDetailRepo.findAllByProductId(productId);
        List<UserReview> reviewsOfProduct = orderDetailsOfProduct
                .stream()
                .flatMap(orderDetail -> reviewRepo.findAllByOrderDetailId(orderDetail.getId()).stream())
                .toList();
        return reviewsOfProduct
                .stream()
                .map(review -> {
                    UserReviewResponse userReviewResponse = new UserReviewResponse();
                    userReviewResponse.setComment(review.getComment());
                    userReviewResponse.setRatingValue(review.getRatingValue());
                    userReviewResponse.setUser(UserMapper.INSTANCE.toUserInfoResponse(review.getUser()));
                    userReviewResponse.setCreateTime(review.getCreateTime());
                    userReviewResponse.setUpdatedTime(review.getUpdatedTime());
                    return userReviewResponse;
                })
                .collect(Collectors.toList());

    }

    public UserReviewResponse addReview(long orderDetailId, UserReviewRequest userReviewRequest) {


        var orderDetail = orderDetailRepo.findById(orderDetailId).orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND));
        var username = SecurityUtils.getCurrentUsername();
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        UserReview userReview = userReviewMapper.toUserReview(userReviewRequest);
        userReview.setOrderDetail(orderDetail);
        userReview.setUser(user);
        reviewRepo.save(userReview);


        return userReviewMapper.toUserReviewResponse(userReview);
    }


}
