package ggs.ggs.service;

import ggs.ggs.domain.Reply;
import ggs.ggs.dto.ReplyDto;

import java.util.*;

public interface ReplyService {
    ReplyDto save(ReplyDto replyDto, Integer memberId);
    void delete(Long idx);
    
    List<ReplyDto> findByBoardId(Long board);
}