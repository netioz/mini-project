package com.playdata.miniproject.board.controller;

import com.playdata.miniproject.board.dto.BoardCommentRequest;
import com.playdata.miniproject.board.dto.BoardCommentResponse;
import com.playdata.miniproject.board.service.BoardCommentService;
import com.playdata.miniproject.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService commentService;
    @PostMapping("/write")
    public String writeComment(
            @RequestBody BoardCommentRequest commentRequest
            , @SessionAttribute(value = "user", required = false) UserDTO user,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 ID 가져오기
        if (user == null) {
            redirectAttributes.addFlashAttribute("loginMessage", "로그인 후 이용 가능합니다.");
            return "redirect:/user/login/first";
        }

        // 댓글 저장 로직 (서비스 클래스를 통해 처리)
        commentRequest.setUserKey(user.getUserKey());
        commentService.saveComment(commentRequest);

        // 응답으로 저장된 댓글 정보 반환
        return "success";
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardCommentResponse>> getComments(@RequestParam int boardId) {
        List<BoardCommentResponse> comments = commentService.getCommentsByBoardId(boardId);
        System.out.println("comments = " + comments);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
