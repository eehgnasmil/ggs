package ggs.ggs.member;

import ggs.ggs.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, Integer>{
    Optional<Member> findByid(String id);
    boolean existsByid(String id);
    boolean existsBynick(String nick);
    boolean existsByemail(String email);
    void deleteById(String id);

    //닉네임 업데이트
    @Modifying
    @Query("UPDATE Member m SET m.nick = :nick WHERE m.idx = :idx")
    void updateNickById(@Param("idx") Integer memberId, @Param("nick") String nick);

    //Pw 업데이트
    @Modifying
    @Query("UPDATE Member m SET m.pw = :pw WHERE m.idx = :idx")
    void updatePwById(@Param("idx") Integer idx, @Param("pw") String pw);

    //주소 업데이트
    @Modifying
    @Query("UPDATE Member m SET m.postcode = :postcode, m.postaddress = :postaddress, m.detailaddress = :detailaddress WHERE m.idx = :idx")
    void modifyAddress(@Param("idx") int idx, @Param("postcode") String postcode, @Param("postaddress") String postaddress, @Param("detailaddress") String detailaddress);

    //인트로 업데이트
    @Modifying
    @Query("UPDATE Member m SET m.intro = :intro WHERE m.id = :id")
    void modifyIntro(@Param("id") String id, @Param("intro") String intro);

    //아이디 찾기(이메일 기준 아이디 받아오기 )
    Optional<Member> findByemail(String email);

    //포인트 수정
    @Modifying
    @Query("UPDATE Member m SET m.point = :newPoint WHERE m = :member")
    void updateMemberPoint(@Param("member") Member member, @Param("newPoint") Integer newPoint);
    

}
