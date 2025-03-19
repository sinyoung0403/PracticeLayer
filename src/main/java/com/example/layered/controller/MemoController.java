package com.example.layered.controller;

import com.example.layered.dto.MemoRequestDto;
import com.example.layered.dto.MemoResponseDto;
import com.example.layered.entity.Memo;
import com.example.layered.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/memos")
public class MemoController {

  private final MemoService memoService;

  /**
   * 생성자 주입
   * 클래스가 필요로 하는 의존성을 생성자를 통해 전달하는 방식
   *
   * @param memoService @Service로 등록된 MemoService 구현체인 Impl
   */
  public MemoController(MemoService memoService) {
    this.memoService = memoService;
  }


  @PostMapping
  public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto) {
    return new ResponseEntity<>(memoService.saveMemo(dto), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<MemoResponseDto>> findAllMemos() {
    return new ResponseEntity<>(memoService.findAllMemos(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id) {
    return new ResponseEntity<>(memoService.findMemoById(id), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MemoResponseDto> updateMemo(
          @PathVariable Long id,
          @RequestBody MemoRequestDto dto
  ) {
    return new ResponseEntity<>(memoService.updateMemo(id, dto.getTitle(), dto.getContents()), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<MemoResponseDto> updateTitle(
          @PathVariable Long id,
          @RequestBody MemoRequestDto dto
  ) {
    return new ResponseEntity<>(memoService.updateTitle(id, dto.getTitle(), dto.getContents()), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {
    memoService.deleteMemo(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
