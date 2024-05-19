package com.example.kubernetespractice.main;

import com.example.kubernetespractice.main.board.BoardDto;
import com.example.kubernetespractice.main.board.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {
  private final BoardService boardService;
  @Value("${app.deployer.name}")
  private String deployerName;

  @GetMapping("/health")
  public String main() {
    return "ok";
  }

  @GetMapping("/boards")
  public ResponseEntity<List<BoardDto>> getAllBoards() {
    return ResponseEntity.ok(boardService.getBoards());
  }

  @GetMapping("/deployer")
  public ResponseEntity<String> getDeployer() {
    return ResponseEntity.ok(deployerName);
  }
}
