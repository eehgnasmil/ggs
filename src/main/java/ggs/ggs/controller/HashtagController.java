package ggs.ggs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ggs.ggs.board.BoardTagRepository;
import ggs.ggs.domain.Hashtag;
import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hash")
@RequiredArgsConstructor
public class HashtagController {

    private final  BoardTagRepository boardTagRepository;

    @GetMapping("/hashtags")
    public ResponseEntity<List<String>> getHashtags() {
        List<Hashtag> hashtags = boardTagRepository.findAll();
        List<String> hashtagNames = hashtags.stream()
                                             .map(Hashtag::getHashtag)
                                             .collect(Collectors.toList());
        return ResponseEntity.ok(hashtagNames);
    }
    
}
