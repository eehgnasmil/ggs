package ggs.ggs.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ggs.ggs.domain.Hashtag;

@Repository
public interface BoardTagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByHashtag(String hashtag);
    
}
