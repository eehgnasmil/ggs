package ggs.ggs.member;

import ggs.ggs.domain.Follow;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    //내가 해당 유저를 팔로우 했는 지 확인하는 쿼리
    boolean existsByFromUserIdAndToUserId(String fromUserId, String toUserId);
    Optional<Follow> findByFromUserIdAndToUserId(String fromUserId, String toUserId);
    List<Follow> findAllByToUserId(String toUserId);
    List<Follow> findAllByFromUserId(String toUserId);

}
