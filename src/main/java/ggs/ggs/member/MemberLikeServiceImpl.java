package ggs.ggs.member;

import ggs.ggs.service.LikeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Qualifier("memberLikeServiceImpl")
@RequiredArgsConstructor
public class MemberLikeServiceImpl implements LikeService {@Override
	public Boolean like(Integer idx, String sid) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int likeNum(Integer idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<Object> likeList(String sid, int page) {
		return null;
	}

}
