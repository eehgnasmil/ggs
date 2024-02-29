package ggs.ggs.service;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;
import ggs.ggs.dto.ReplyDto;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyService {
    ReplyDto save(ReplyDto replyDto, Integer memberId);
    void delete(Long idx);
    
    List<ReplyDto> findByBoardId(Long board);



}