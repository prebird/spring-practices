package com.example.kubernetespractice.main;

import com.example.kubernetespractice.main.board.BoardDto;
import com.example.kubernetespractice.main.board.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {
  private final BoardService boardService;

  @GetMapping("/health")
  public String main() {
    return "ok";
  }

  @GetMapping("/boards")
  public ResponseEntity<List<BoardDto>> getAllBoards() {
    return ResponseEntity.ok(boardService.getBoards());
  }
}
