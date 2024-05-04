package com.example.kubernetespractice.main.board;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;

  public List<BoardDto> getBoards() {
    return boardRepository.findAll().stream().map(BoardDto::from).collect(Collectors.toList());
  }
}
