package com.example.layered.entity;

import com.example.layered.dto.MemoRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Memo {
  // filed 를 정의한다
  private Long id;
  private String title;
  private String contents;

  // title 과 contents 만 받을 생성자
  public Memo(String title, String contents) {
    this.title = title;
    this.contents = contents;
  }

  // 전체 Update
  public void update(String title, String contents) {
    this.title = title;
    this.contents = contents;
  }

  // 부분 Update
  public void updateTitle(String title) {
    this.title = title;
  }
}
