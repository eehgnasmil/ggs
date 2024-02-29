package ggs.ggs.dto;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Goods;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;
import ggs.ggs.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private Long idx;
    private Integer member;
    private String nickname;
    private Long board;
    private String comment;
    private Long parentIdx;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<ReplyDto> childReplies = new ArrayList<>();


    public Reply toEntity(Member member, Board board, Reply parent) {
        return Reply.builder()
                .comment(this.comment)
                .member(member)
                .board(board)
                .parent(parent)
                .build();
    }

    public static ReplyDto from(Reply reply) {
        ReplyDto dto = new ReplyDto(
                reply.getIdx(),
                reply.getMember() != null ? reply.getMember().getIdx() : null,
                reply.getMember() != null ? reply.getMember().getNick() : null, // member가 null이 아닐 때만 getNick() 호출
                reply.getBoard() != null ? reply.getBoard().getIdx() : null,
                reply.getComment(),
                reply.getParent() != null ? reply.getParent().getIdx() : null,
                reply.getCreatedDate(),
                reply.getModifiedDate(),
                new ArrayList<>());

        // 대댓글 설정
        if (reply.getChildReplies() != null) {
            for (Reply childReply : reply.getChildReplies()) {
                dto.getChildReplies().add(from(childReply));
            }
        }

        return dto;
    }
}
