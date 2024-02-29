package ggs.ggs.member;

import ggs.ggs.service.LikeService;
import ggs.ggs.service.ReportService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Qualifier("memberReportServiceImpl")
@RequiredArgsConstructor
public class MemberReportServiceImpl implements ReportService {
	@Override
	public Boolean report(Integer idx, String sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int reportNum(Integer idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<Object> reportList(String sid, int page) {
		return null;
	}

}
