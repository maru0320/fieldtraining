import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./Notice.css";
import Sidebar from "../../Sidebar";
import axios from "axios";

const Notice = () => {
  const [boardList, setBoardList] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize] = useState(5);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [tempSearchKeyword, setTempSearchKeyword] = useState(""); // 입력 중인 키워드
  const [error, setError] = useState(null);
  const [currentUserID, setCurrentUserID] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const navigate = useNavigate();

  const fetchBoardList = async () => {
    try {
      const response = await axios.get("http://localhost:8090/notice/list", {
        params: {
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
    fetchBoardList();
    // 토큰 디코딩
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        setCurrentUserID(decodedToken.userId);
        setUserRole(decodedToken.roles);
      } catch (error) {
        console.error("토큰 디코딩 실패:", error);
      }
    }
  }, [currentPage]); // 페이지 변경 시 데이터 가져오기

  useEffect(() => {
    setCurrentPage(0); // 검색 키워드 변경 시 페이지 초기화
    fetchBoardList();
  }, [searchKeyword]); // 검색 키워드 변경 시 데이터 가져오기

  const handleSearch = (e) => {
    e.preventDefault();
    setSearchKeyword(tempSearchKeyword); // 검색 키워드 업데이트
  };

  const handleWriteClick = () => {
    navigate("/Notice/NoticeWrite");
  };

  const handleDetailClick = (boardID) => {
    navigate(`/Notice/NoticeBoardDetail/${boardID}`);
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
    <div className="board-body">
        <Sidebar />
      <form className="board-container" onSubmit={handleSearch}>
        <h1 className="board-title">공지사항</h1>
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

                  {board.writerRole === "schoolManager" ? (
                    <td className="schoolManager">
                      <span className="icon school-icon"></span> {board.writerName}
                    </td>
                  ) : board.writerRole === "collegeManager" ? (
                    <td className="collegeManager">
                      <span className="icon college-icon"></span> {board.writerName}
                    </td>
                  ) : (
                    <td>작성자 없음</td>
                  )}
                  <td>{board.date}</td>
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
          {currentUserID && 
              (userRole?.includes("ROLE_SCHOOL_MANAGER") || userRole?.includes("ROLE_COLLEGE_MANAGER") || userRole?.includes("ROLE_ADMIN")) ? (
          <button onClick={handleWriteClick} className="search-button">
            글쓰기
          </button>
              ) : (<></>)}
        </div>
        <div className="pagination">{renderPageButtons()}</div>
      </form>
    </div>
  );
};

export default Notice;
