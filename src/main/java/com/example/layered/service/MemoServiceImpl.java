package com.example.layered.service;

import com.example.layered.dto.MemoRequestDto;
import com.example.layered.dto.MemoResponseDto;
import com.example.layered.entity.Memo;
import com.example.layered.repository.MemoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

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

    return memoRepository.saveMemo(memo);
  }

  @Override
  public List<MemoResponseDto> findAllMemos() {
    // Repository 에 있는 데이터를 가져와야함.
    return memoRepository.findAllMemos();
  }

  @Override
  public MemoResponseDto findMemoById(Long id) {
    Memo memo = memoRepository.findMemoByIdOrElseThrow(id);
    return new MemoResponseDto(memo);
  }

  @Transactional
  @Override
  public MemoResponseDto updateMemo(Long id, String title, String contents) {

    if (title == null || contents == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
    }

    int updateRow = memoRepository.updateMemo(id, title, contents); //

    if (updateRow == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }

    Memo memo = memoRepository.findMemoByIdOrElseThrow(id);
    // 업데이트는 성공적으로 실행되었지만, 조회가 실패할 경우 아예 이 작업을 모두 롤백 하게끔 처리
    // 그래서 Transactional 을 붙이는 것.

    return new MemoResponseDto(memo);
  }

  @Transactional
  @Override
  public MemoResponseDto updateTitle(Long id, String title, String contents) {

    // 필수 값 검증
    if (title == null || contents != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
    }

    int updateTitleRow = memoRepository.updateTitle(id, title);

    if (updateTitleRow == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }

    Memo memo = memoRepository.findMemoByIdOrElseThrow(id);

    return new MemoResponseDto(memo);
  }

  @Override
  public void deleteMemo(Long id) {
    int deleteMemoRow = memoRepository.deleteMemo(id);

    if (deleteMemoRow == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + id);
    }
  }
}
