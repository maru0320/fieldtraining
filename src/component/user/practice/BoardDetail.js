import React, { useState, useEffect, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import "./BoardDetail.css";

const BoardDetail = () => {
  const location = useLocation();
  const { boardID } = location.state;
  const navigate = useNavigate();
  const [boardDetail, setBoardDetail] = useState(null);

  const fetchBoardDetail = useCallback(async () => {
    try {
      const response = await axios.get(
        `http://localhost:8090/practice/detail?boardID=${boardID}`
      );
      setBoardDetail(response.data);
    } catch (error) {
      console.error("게시글 상세 정보 가져오기 실패:", error);
    }
  }, [boardID]);

  useEffect(() => {
    fetchBoardDetail();
  }, [fetchBoardDetail]);

  const deleteBoard = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8090/practice/delete?boardID=${boardID}`
      );
      console.log("게시글 삭제 성공:", response);
      alert("게시글이 삭제되었습니다!");
      navigate("/practice");
    } catch (error) {
      console.error("게시글 삭제 실패:", error);
    }
  };

  const handleUpdateClick = () => {
    navigate("/PracticeUpdate", { state: { boardID } });
  };

  if (!boardDetail) {
    return <div>게시글 정보를 불러오지 못했습니다.</div>;
  }

  return (
    <div className="container">
      <div className="board-header">
        <h1 className="board-title">{boardDetail.title}</h1>
        <div className="board-writer">
          <p>작성자: {boardDetail.writerName}</p>
          <p>등록일: {boardDetail.trainingDate}</p>
        </div>
      </div>
      <div className="board-content">
        <p>{boardDetail.content}</p>
      </div>
      <div className="board-files">
        {boardDetail.files.map((file, index) => (
          <div key={index}>
            <p>
              {file.originalName}{" "}
              <a href={file.storedPath} download>
                다운로드
              </a>
            </p>
          </div>
        ))}
      </div>
      <div className="board-buttons">
        <button onClick={handleUpdateClick} className="action-button update-button">
          수정
        </button>
        <button onClick={deleteBoard} className="action-button delete-button">
          삭제
        </button>
      </div>
    </div>
  );
};

export default BoardDetail;
