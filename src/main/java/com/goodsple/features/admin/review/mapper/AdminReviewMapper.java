package com.goodsple.features.admin.review.mapper;

import com.goodsple.features.admin.review.dto.AdminReviewDetail;
import com.goodsple.features.admin.review.dto.AdminReviewListItem;
import com.goodsple.features.admin.review.dto.AdminReviewSearchCond;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminReviewMapper {
    List<AdminReviewListItem> selectReviews(@Param("cond") AdminReviewSearchCond cond,
                                            @Param("limit") int limit,
                                            @Param("offset") int offset);
    long countReviews(@Param("cond") AdminReviewSearchCond cond);

    AdminReviewDetail selectReviewDetail(@Param("reviewId") Long reviewId);
    List<String> selectReviewPhotos(@Param("reviewId") Long reviewId);

    int updateReviewStatus(@Param("reviewId") Long reviewId,
                           @Param("status") String status);
}
