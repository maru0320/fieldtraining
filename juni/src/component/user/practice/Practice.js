import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Practice.css"; 
import Sidebar from "../../Sidebar";
const Practice = () => {
  const [boardList, setBoardList] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState(""); // 검색어
  const navigate = useNavigate();
    const handleWriteClick = () => {
        navigate("/PracticeWrite");
    };
    const handleDetailClick = (boardID) => {
      navigate("/BoardDetail", { state: { boardID } });
    };
  // 게시판 목록 가져오기
  const fetchBoardList = async () => {
    try {
      const response = await axios.get("http://localhost:8090/practice/list");
      setBoardList(response.data);
    } catch (error) {
      console.error("게시판 목록 가져오기 실패:", error);
    }
  };
  // 게시판 검색
  const handleSearch = async () => {
    try {
      const response = await axios.post("http://localhost:8090/practice/search", {
        keyword: searchKeyword,
      });
      setBoardList(response.data);
    } catch (error) {
      console.error("검색 실패:", error);
    }
  };
  // 컴포넌트가 마운트되면 목록 가져오기 실행
  useEffect(() => {
    fetchBoardList();
  }, []);
  return (
    <div>
    <Sidebar />
    

    <div className="board-container">
    
      <h1 className="board-title">공지사항</h1>
      <table className="board-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {boardList.length === 0 ? (
            <tr>
              <td colSpan="3" className="no-data">
                검색 결과가 없습니다.
              </td>
            </tr>
          ) : (
            boardList.map((board, index) => (
              <tr key={board.id} onClick={() => handleDetailClick(board.boardID)}>
                <td>{index + 1}</td>
                <td>{board.title}</td>
                <td>{board.trainingDate}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
      <div className="board-footer">
        <input
          type="text"
          placeholder="검색..."
          value={searchKeyword}
          onChange={(e) => setSearchKeyword(e.target.value)}
          className="search-input"
        />
        <button onClick={handleWriteClick} className="search-button">
          글쓰기
        </button>
        <button onClick={handleSearch} className="search-button">
          검색
        </button>
      </div>
    </div>
    </div>
  );
};
export default Practice;