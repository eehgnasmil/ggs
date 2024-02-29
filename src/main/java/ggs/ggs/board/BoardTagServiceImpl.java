package ggs.ggs.board;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.domain.MiddleTag;
import ggs.ggs.dto.HashtagCountDto;
import ggs.ggs.service.HashtagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardTagServiceImpl implements HashtagService {

    private final BoardTagRepository hashtagRepository;
    private final BoardMiddleTagRepository middleTagRepository;

    @Override
    public Hashtag createHashtag(String hashtag, Board board) {
        // 해시태그가 이미 존재하는지 확인
        Optional<Hashtag> existingHashtag = hashtagRepository.findByHashtag(hashtag);
        Hashtag savedHashtag;
        if (existingHashtag.isPresent()) {
            // 이미 존재하는 해시태그라면 그대로 반환
            savedHashtag = existingHashtag.get();
        } else {
            // 존재하지 않는 해시태그라면 새로 생성
            Hashtag newHashtag = new Hashtag();
            newHashtag.setHashtag(hashtag);
            savedHashtag = hashtagRepository.save(newHashtag);
        }

        // MiddleTag 엔티티 생성 및 정보 설정
        MiddleTag middleTag = new MiddleTag();
        middleTag.setBoard(board);
        middleTag.setHashtag(savedHashtag);

        // MiddleTag 엔티티 저장
        middleTagRepository.save(middleTag);

        return savedHashtag;
    }

    @Override
    public List<Hashtag> getAllHashtags() {
        return hashtagRepository.findAll();
    }

    @Override
    public List<Hashtag> getHashtagsByBoard(Board board) {
        List<MiddleTag> middleTags = middleTagRepository.findByBoard(board);

        return middleTags.stream()
                .map(MiddleTag::getHashtag)
                .collect(Collectors.toList());

    }

    @Transactional
    public void updateHashtags(List<String> newHashtags, Board board) {
        // 기존에 게시판과 연결된 해시태그 제거
        List<MiddleTag> middleTags = middleTagRepository.findByBoard(board);
        middleTagRepository.deleteAll(middleTags);

        // 새로운 해시태그 연결
        for (String hashtag : newHashtags) {
            createHashtag(hashtag, board);
        }
    }

    @Override
    public List<HashtagCountDto> getTop7Hashtags() {
        Pageable topTen = PageRequest.of(0, 7);
        List<Object[]> topHashtags = middleTagRepository.findTopHashtags(topTen);

        return topHashtags.stream()
                .map(result -> new HashtagCountDto((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }
}
