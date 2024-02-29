package ggs.ggs.service;

import org.springframework.data.domain.Page;

public interface ReportService {
    Boolean report(Integer idx, String sid);

    // 내가 좋아요 한 개수
    int reportNum(Integer idx);

    Page<Object> reportList(String sid, int page);

}
