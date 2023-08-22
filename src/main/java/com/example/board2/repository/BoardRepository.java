package com.example.board2.repository;

import com.example.board2.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> { // JpaRepository를 상속받아서 사용 (Board 테이블의 id는 Integer 타입)
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable); // 제목으로 검색하는 메소드 (findBy + 컬럼명 + Containing)
}
