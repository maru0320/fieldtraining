import React, { useState, useEffect, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import './BoardDetail.css';

const BoardDetail = () => {
  const location = useLocation();
  const { boardID } = location.state;
  const navigate = useNavigate();
  const [boardDetail, setBoardDetail] = useState(null);

  // JWT에서 현재 사용자 ID 가져오기
  const token = localStorage.getItem("token");
  const decodedToken = jwtDecode(token);
  const currentUserID = decodedToken.sub;

  console.log("현재 사용자 ID:", currentUserID);

  // 게시글 상세 정보 가져오기
  const fetchBoardDetail = useCallback(async () => {
    try {
      const response = await axios.get(
        `http://localhost:8090/practice/detail?boardID=${boardID}`
      );
      console.log("게시글 상세 정보:", response.data);
      setBoardDetail(response.data);
    } catch (error) {
      console.error("게시글 상세 정보 가져오기 실패:", error);
    }
  }, [boardID]);

  useEffect(() => {
    fetchBoardDetail();
  }, [fetchBoardDetail]);

  // 게시글 삭제
  const deleteBoard = async () => {
    try {
      await axios.get(
        `http://localhost:8090/practice/delete?boardID=${boardID}`
      );
      alert("게시글이 삭제되었습니다!");
      navigate("/practice");
    } catch (error) {
      console.error("게시글 삭제 실패:", error);
    }
  };

  // 게시글 수정 버튼 클릭
  const handleUpdateClick = () => {
    navigate("/Practice/PracticeUpdate", { state: { boardID } });
  };

  if (!boardDetail) {
    return <div>게시글 정보를 불러오지 못했습니다.</div>;
  }

  return (
    <div className="container">
      <form className="board-form">
        <table className="board-detail-table">
          <thead>
            <tr>
              <th colSpan="2" className="board-title-header">
                <h1 className="board-title">{boardDetail.title}</h1>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="label">작성자</td>
              <td>{boardDetail.writerName}</td>
            </tr>
            <tr>
              <td className="label">등록일</td>
              <td>{boardDetail.trainingDate}</td>
            </tr>
            <tr>
              <td className="label2">내용</td>
              <td>
                <div className="board-content">{boardDetail.content}</div>
              </td>
            </tr>
            {boardDetail.files.length > 0 && (
              <tr>
                <td className="label">첨부 파일</td>
                <td>
                  <div className="board-files">
                    {boardDetail.files.map((file, index) => (
                      <p key={index}>
                        {file.originalName}{" "}
                        <a href={file.storedPath} download>
                          다운로드
                        </a>
                      </p>
                    ))}
                  </div>
                </td>
              </tr>
            )}
          </tbody>
        </table>
        {/* 조건부 렌더링: 작성자와 로그인한 사용자가 동일할 때만 버튼 표시 */}
        {String(boardDetail.writerID) === String(currentUserID) && (
          <div className="board-buttons">
            <button
              onClick={handleUpdateClick}
              className="action-button update-button"
              type="button"
            >
              수정
            </button>
            <button
              onClick={deleteBoard}
              className="action-button delete-button"
              type="button"
            >
              삭제
            </button>
          </div>
        )}
      </form>
    </div>
  );
  
};

export default BoardDetail;
