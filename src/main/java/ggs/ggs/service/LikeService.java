package ggs.ggs.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LikeService {
    Boolean like(Integer idx, String sid);

    // 내가 좋아요 한 개수
    int likeNum(Integer idx);

    Page<Object> likeList(String sid, int page);

   

}
