package com.example.layered.service;

import com.example.layered.dto.MemoRequestDto;
import com.example.layered.dto.MemoResponseDto;
import com.example.layered.entity.Memo;
import com.example.layered.repository.MemoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
public class MemoServiceImpl implements MemoService {

  private final MemoRepository memoRepository;

  public MemoServiceImpl(MemoRepository memoRepository) {
    this.memoRepository = memoRepository;
  }

  @Override
  public MemoResponseDto saveMemo(MemoRequestDto dto) {
    // 요청받은 데이터로 Memo 객체 생성 ID 값은 없음
    Memo memo = new Memo(dto.getTitle(), dto.getContents());

    // DB 에 저장
    Memo savedMemo = memoRepository.saveMemo(memo);

    return new MemoResponseDto(savedMemo);
  }

  @Override
  public List<MemoResponseDto> findAllMemos() {
    // Repository 에 있는 데이터를 가져와야함.
    return memoRepository.findAllMemos();
  }

  @Override
  public MemoResponseDto findMemoById(Long id) {
    //
    Memo memo = memoRepository.findMemoById(id);
    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }
    return new MemoResponseDto(memo);
  }

  @Override
  public MemoResponseDto updateMemo(Long id, String title, String contents) {
    Memo memo = memoRepository.findMemoById(id);

    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }

    if (title == null || contents == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
    }

    memo.update(title,contents);

    return new MemoResponseDto(memo);
  }

  @Override
  public MemoResponseDto updateTitle(Long id, String title, String contents) {
    Memo memo = memoRepository.findMemoById(id);

    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }

    if (title == null || contents != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
    }

    memo.updateTitle(title);

    return new MemoResponseDto(memo);
  }

  @Override
  public void deleteMemo(Long id) {
    Memo memo = memoRepository.findMemoById(id);

    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }

    memoRepository.deleteMemo(id);
  }
}
