package com.example.board2.controller;

import com.example.board2.entity.Board;
import com.example.board2.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

  @Autowired // 의존성 주입  (BoardService 객체를 주입받음)
  private BoardService boardService;

  // 게시글 작성 폼
  @GetMapping("/board/write") // http://localhost:8080/board/write -> boardWriteForm() 실행
  public String boardWriteForm(){

    return "boardwrite";
  }

  // 게시글 작성
  @PostMapping("/board/writepro") // http://localhost:8080/board/writepro -> boardWritePro() 실행
  public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{
    boardService.write(board, file); // BoardService의 write() 메소드 호출

    model.addAttribute("message","글 작성 완료"); // Model 객체에 msg를 담음
    //model.addAttribute("message","글 작성 실패");
    model.addAttribute("searchUrl","/board/list"); // Model 객체에 searchUrl를 담음
    return "message";
  }

  // 게시글 리스트
  @GetMapping("/board/list")
  public String boardList(Model model,
                          @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
                          Pageable pageable,
                          String searchKeyword){

    Page<Board> list = null; // Page 객체를 담을 변수 list를 선언

    if(searchKeyword == null){
       list = boardService.boardList(pageable ); // BoardService의 boardList() 메소드 호출 (페이징 처리)
    }else{
      list = boardService.boardSearchList(searchKeyword, pageable); // BoardService의 boardSearchList() 메소드 호출 (검색
    }


    int nowPage = list.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작) -> 0이 현재 페이지 번호라면 1이 현재 페이지 번호가 됨
    int startPage = Math.max(nowPage - 4,1); // 시작 페이지 번호 (1부터 시작) -> 현재 페이지 번호가 5라면 1이 시작 페이지 번호가 됨
    int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 끝 페이지 번호 (총 페이지 번호) -> 총 페이지 번호가 10이라면 10이 끝 페이지 번호가 됨

    model.addAttribute("list",list); // Model 객체에 list를 담음 (게시글 목록)
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    // model.addAttribute("list",boardService.boardList(pageable)); // Model 객체에 list를 담음 (게시글 목록
    return "boardlist";
  }

  // 게시글 상세보기
  @GetMapping("/board/view")
  public String boardView(Model model, Integer id){

    model.addAttribute("board", boardService.boardView(id));
    return "boardview";
  }
  // 게시글 삭제
  @GetMapping("/board/delete")
  public String boardDelete(Integer id){
    boardService.boardDelete(id);
    return "redirect:/board/list"; // 게시글 삭제 후 게시글 목록으로 이동  (redirect:/board/list)
  }

  // 게시글 수정게시문 불러오기
  @GetMapping("board/modify/{id}")
  public String boardModify(@PathVariable("id") Integer id,
                            Model model){ // @PathVariable 어노테이션을 사용하여 id를 매개변수로 받음 (PathVariable 어노테이션은 URL 경로에 변수를 넣어주는 역할)

    model.addAttribute("board", boardService.boardView(id)); // Model 객체에 board를 담음 (게시글 상세보기)

    return "boardmodify";

  }
  // 게시글 수정
  @PostMapping("/board/update/{id}")
  public String boardUpdate(@PathVariable("id") Integer id,
                            Board board, Model model,
                            MultipartFile file) throws Exception{
    Board boardTemp = boardService.boardView(id); // BoardService의 boardview() 메소드 호출 (게시글 상세보기)
    boardTemp.setTitle(board.getTitle()); // 게시글 수정 (제목)
    boardTemp.setContent(board.getContent()); // 게시글 수정 (내용)

    boardService.write(boardTemp, file); // BoardService의 write() 메소드 호출 (게시글 수정)

    model.addAttribute("message","글 수정 완료");
    //model.addAttribute("message","글 수정 실패");
    model.addAttribute("searchUrl","/board/list");

    return "message";  // 게시글 수정 후 메시지 출력
  }
}
