package com.example.layered.repository;

import com.example.layered.dto.MemoResponseDto;
import com.example.layered.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemoRepository implements MemoRepository{

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplateMemoRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public MemoResponseDto saveMemo(Memo memo) {
    // 데이터를 삽입하는데 편리하게 사용할 수 있는 클래스.
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    // 이 테이블의 키값은 아이디라는 이름으로 설정 돼 있다
    // 테이블에 데이터를 삽입하고 지정한다. 음
    jdbcInsert.withTableName("memo").usingGeneratedKeyColumns("id");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("title",memo.getTitle());
    parameters.put("contents",memo.getContents());

    // executeAndReturnKey() 메서드는 parameters에 담긴 데이터를 memo 테이블에 삽입하고,
    // 그 후 생성된 id 값을 반환합니다. 이 반환된 값은 key 변수에 저장됩니다.
    // 여기서 new MapSqlParameterSource 는 파라미터 값을 Map 형식으로 저장하고
    // 이를 SQL 실행 시 사용할 수 있도록 JdbcTemplate 에 전달하는 역할을 함.
    // INSERT INTO memo (title, contents) VALUES (:title, :contents) 이러한 역할을 한다고 보면 된다.
    Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

    return new MemoResponseDto(key.longValue(), memo.getTitle(), memo.getContents());
  }

  @Override
  public List<MemoResponseDto> findAllMemos() {
    return jdbcTemplate.query("SELECT * FROM memo", memoRowMapper());
  }

  @Override
  public Optional<Memo> findMemoById(Long id) {
    List<Memo> result = jdbcTemplate.query("SELECT * FROM memo WHERE id = ?", memoRowMapperV2(), id);

    return result.stream().findAny();
  }

  @Override
  public Memo findMemoByIdOrElseThrow(Long id) {
    List<Memo> result = jdbcTemplate.query("select * from memo where id = ?", memoRowMapperV2(), id);
    return result.stream().findAny().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id));
  }

  @Override
  public int updateMemo(Long id, String title, String contents) {
    // 반영된 row 의 수가 updateRow 에 반환되게 됨.
    return jdbcTemplate.update("update memo set title =? , contents = ? where id = ? ", title, contents, id);
  }

  @Override
  public int updateTitle(Long id, String title) {
    return jdbcTemplate.update("update memo set title = ? where id = ? ", title, id);
  }

  @Override
  public int deleteMemo(Long id) {
    return jdbcTemplate.update("delete from memo where id = ?", id);
  }

  // 이 클래스 내부에서만 쓸 함수임.
  private RowMapper<MemoResponseDto> memoRowMapper() {
    return new RowMapper<MemoResponseDto>() {
      @Override
      public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MemoResponseDto(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("contents")
        );
      }
    };
  }

  private RowMapper<Memo> memoRowMapperV2() {
    return new RowMapper<Memo>() {
      @Override
      public Memo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Memo (
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("contents")
        );
      }
    };
  }
}
