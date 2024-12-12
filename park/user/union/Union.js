import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./Union.css";
import Sidebar from "../../Sidebar";
import axiosInstance from "../../axiosInstance";

const Union = () => {
  const token = localStorage.getItem("token");
  const [boardList, setBoardList] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize] = useState(5);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [tempSearchKeyword, setTempSearchKeyword] = useState(""); // 입력 중인 키워드
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const decodedToken = jwtDecode(token);
  const sub = decodedToken.userId;

  const fetchBoardList = async () => {
    try {
      const response = await axiosInstance.get("/union/list", {
        params: {
          userId: sub,
          page: currentPage,
          size: pageSize,
          keyword: searchKeyword || "",
        },
      });
      setBoardList(response.data.content);
      setTotalPages(response.data.totalPages);
      setError(null);
    } catch (err) {
      console.error("게시판 목록 가져오기 실패:", err);
      setError("게시판 목록을 가져오는데 실패했습니다. 다시 시도해주세요.");
    }
  };

  useEffect(() => {
    if (sub) fetchBoardList();
  }, [currentPage]); // 페이지 변경 시 데이터 가져오기

  useEffect(() => {
    setCurrentPage(0); // 검색 키워드 변경 시 페이지 초기화
    if (sub) fetchBoardList();
  }, [searchKeyword]); // 검색 키워드 변경 시 데이터 가져오기

  const handleSearch = (e) => {
    e.preventDefault();
    setSearchKeyword(tempSearchKeyword); // 검색 키워드 업데이트
  };

  const handleWriteClick = () => {
    navigate("/Union/UnionWrite");
  };

  const handleDetailClick = (boardID) => {
    navigate(`/Union/UnionBoardDetail/${boardID}`);
  };

  const renderPageButtons = () => {
    return Array.from({ length: totalPages }, (_, index) => (
      <button
        key={index}
        onClick={() => setCurrentPage(index)} // 클릭한 페이지로 상태 업데이트
        className={`page-button ${currentPage === index ? "active" : ""}`}
      >
        {index + 1}
      </button>
    ));
  };

  return (
    <div className="mypage-body">
      <div className="sidebar-container">
        <Sidebar />
      </div>
      <form className="board-container" onSubmit={handleSearch}>
        <h1 className="board-title">교직실무일지</h1>
        {error && <p className="error-message">{error}</p>}
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
            {boardList.length === 0 ? (
              <tr>
                <td colSpan="4" className="no-data">
                  검색 결과가 없습니다.
                </td>
              </tr>
            ) : (
              boardList.map((board, index) => (
                <tr
                  key={board.boardID}
                  onClick={() => handleDetailClick(board.boardID)}
                >
                  <td>{currentPage * pageSize + index + 1}</td>
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
              value={tempSearchKeyword}
              onChange={(e) => setTempSearchKeyword(e.target.value)}
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
        <div className="pagination">{renderPageButtons()}</div>
      </form>
    </div>
  );
};

export default Union;
