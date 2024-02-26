package ggs.ggs.service;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import java.util.*;

public interface HashtagService {
    Hashtag createHashtag(String hashtag, Board board);
    List<Hashtag> getAllHashtags();
    List<Hashtag> getHashtagsByBoard(Board board);
    
    // Hashtag getHashtag(String hashtag);
    // List<Hashtag> getAllHashtags();
    // Hashtag updateHashtag(String oldHashtag, String newHashtag);
    // void deleteHashtag(String hashtag);
}
