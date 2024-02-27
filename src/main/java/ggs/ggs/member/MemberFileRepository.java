package ggs.ggs.member;

import ggs.ggs.domain.MemberFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberFileRepository extends JpaRepository<MemberFile, Integer> {

    List<MemberFile> findByMemberIdx(Integer MemberIdx);
}
