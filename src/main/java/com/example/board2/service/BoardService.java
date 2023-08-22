package com.example.board2.service;

import com.example.board2.entity.Board;
import com.example.board2.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

  @Autowired
    private BoardRepository boardRepository; //  BoardRepository 객체를 주입받음

  // 게시글 작성
    public void write(Board board , MultipartFile file) throws Exception{ // BoardService의 write() 메소드  -> BoardController의 boardWritePro() 메소드에서 호출
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 현재 프로젝트의 경로를 가져옴

      UUID uuid = UUID.randomUUID(); // 랜덤한 문자열을 생성

      String fileName = uuid + "_" + file.getOriginalFilename(); // 랜덤한 문자열과 파일명을 합침

      File saveFile = new File(projectPath, fileName); // 파일을 저장할 경로와 파일명을 지정
      file.transferTo(saveFile); // 파일을 저장

      board.setFilename(fileName); // 저장된 파일의 이름
      board.setFilepath("/files/" + fileName); // 저장된 파일 경로의 이름

        boardRepository.save(board); // BoardRepository의 save() 메소드 호출 (Board 객체를 저장)
    }

    // 게시글 리스트
  public Page<Board> boardList(Pageable pageable){
      return boardRepository.findAll(pageable); // BoardRepository의 findAll() 메소드 호출 (Board 테이블의 모든 데이터를 가져옴) -> BoardController의 boardList() 메소드에서 호출 (페이징 처리)
  }

  //  게시글 검색 (제목으로 검색)
  public Page<Board> boardSearchList(String searchKeyword,Pageable pageable){
      return boardRepository.findByTitleContaining(searchKeyword,pageable);
  }

  // 게시글 상세보기
  public Board boardView(Integer id){

      return boardRepository.findById(id).get(); // BoardRepository의 findById() 메소드 호출 (Board 테이블의 id를 기준으로 데이터를 가져옴)
  }
  // 게시글 삭제
  public void boardDelete(Integer id){

      boardRepository.deleteById(id);
  }
}
