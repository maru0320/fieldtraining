import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./Practice.css";
import Sidebar from "../../Sidebar";

const Practice = () => {
  const token = localStorage.getItem("token"); // 로컬 스토리지에서 JWT 토큰 가져오기
  const [boardList, setBoardList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const [pageSize] = useState(5); // 페이지당 게시물 수
  const [searchKeyword, setSearchKeyword] = useState(""); // 검색어
  const navigate = useNavigate();

  const handleWriteClick = () => {
    navigate("/Practice/PracticeWrite");
  };
  const handleDetailClick = (boardID) => {
    navigate("/Practice/BoardDetail", { state: { boardID } });
  };

  // 토큰에서 사용자 ID 디코딩
  const decodedToken = jwtDecode(token);
  const sub = decodedToken.sub;

  // 게시판 목록 가져오기
  useEffect(() => {
    const fetchBoardList = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8090/practice/list?id=${sub}`
        );
        setBoardList(response.data);
      } catch (error) {
        console.error("게시판 목록 가져오기 실패:", error);
      }
    };
  
    fetchBoardList();
  }, [sub]); // sub를 의존성 배열에 포함
  

  // 게시판 검색
  const handleSearch = async (e) => {
    e.preventDefault(); // 버튼 클릭 시 기본 동작 방지
    try {
      const response = await axios.post("http://localhost:8090/practice/search", {
        keyword: searchKeyword,
        boardList: boardList,
      });
      setBoardList(response.data);
      if (searchKeyword) {
        setCurrentPage(1); // 검색어가 입력된 경우에만 1페이지로 이동
      }
    } catch (error) {
      console.error("검색 실패:", error);
    }
  };

  // 현재 페이지에 해당하는 데이터 슬라이싱
  const paginatedData = boardList.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  // 총 페이지 수 계산
  const totalPages = Math.ceil(boardList.length / pageSize);

  // 페이지 버튼 렌더링
  const renderPageButtons = () => {
    return Array.from({ length: totalPages }, (_, index) => (
      <button
        key={index}
        onClick={() => setCurrentPage(index + 1)}
        className={`page-button ${
          currentPage === index + 1 ? "active" : ""
        }`}
      >
        {index + 1}
      </button>
    ));
  };

  return (
    <div className="mypage-body">
      {/* 사이드바 */}
      <div className="sidebar-container">
        <Sidebar />
      </div>
      <form className="board-container" onSubmit={handleSearch}>
        <h1 className="board-title">실습일지</h1>
        <table className="board-table">
          <thead>
            <tr>
              <th>번호</th>
              <th>제목</th>
              <th>작성자</th>
              <th>작성일</th>
            </tr>
          </thead>
          <tbody>
            {paginatedData.length === 0 ? (
              <tr>
                <td colSpan="4" className="no-data">
                  검색 결과가 없습니다.
                </td>
              </tr>
            ) : (
              paginatedData.map((board, index) => (
                <tr
                  key={board.boardID}
                  onClick={() => handleDetailClick(board.boardID)}
                >
                  <td>
                    {/* index는 현재 페이지 기준으로 계산 */}
                    {(currentPage - 1) * pageSize + index + 1}
                  </td>
                  <td>{board.title}</td>
                  <td>{board.writerName}</td>
                  <td>{board.trainingDate}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        <div className="board-footer">
          <div className="search">
            <input
              type="text"
              placeholder="제목, 내용"
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              className="search-input"
            />
            <button type="submit" className="search-button">
              검색
            </button>
          </div>
          <button onClick={handleWriteClick} className="search-button">
            글쓰기
          </button>
        </div>
        {/* 페이지 버튼 렌더링 */}
        <div className="pagination">{renderPageButtons()}</div>
      </form>;
    </div>
  );
};

export default Practice;
