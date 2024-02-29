package ggs.ggs.board;

import ggs.ggs.domain.Hashtag;
import ggs.ggs.dto.BoardDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardDto getBoardById(Long idx);

    List<BoardDto> getBoardsByCategory(String category);

    Page<BoardDto> getAllBoards(String category, String bsort, Pageable pageable);

    void incrementViewCount(Long idx);

    List<BoardDto> getAllBoard(String category, String bsort);

    void updateBoard(Long idx, BoardDto updatedBoardDto);

    void deleteBoard(Long idx, Integer userId);

    BoardDto createBoard(BoardDto dto, String id);

    Page<BoardDto> getMyboards(String id, int page);

    List<Hashtag> getHashtagsForBoard(Long boardIdx);

    Page<BoardDto> getBoardsByHashtag(String hashtag, int page, int size);

    Page<BoardDto> getBoardsByUserReplies(String id, int page);


}
