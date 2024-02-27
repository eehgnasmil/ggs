package ggs.ggs.member;

import ggs.ggs.domain.Follow;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.MemberDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MemberService {
    String check(Map<String, String> checks);

    Member create(MemberDto dto) throws IOException;

    boolean followCheck(String sid, String id);

    MemberDto memberList(String id);

    List<Follow> followerList(String id);

    List<Follow> followList(String id);

    void updateIntro(String id, String intro);

    void updateFollow(String toUserId, String fromUserId);

    void unFollow(String toUserId, String fromUserId);
}
