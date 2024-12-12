import React, { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import "./PracticeBoardDetail.css";
import axiosInstance from "../../axiosInstance";

const PracticeBoardDetail = () => {
  const { boardID } = useParams(); // URL에서 boardID 추출
  const navigate = useNavigate();
  const [boardDetail, setBoardDetail] = useState(null);
  const [content, setContent] = useState("");

  const token = localStorage.getItem("token");
  const decodedToken = jwtDecode(token);
  const currentUserID = decodedToken.userId;
  const role = decodedToken.roles;

  // 게시글 상세 정보 가져오기
  const fetchBoardDetail = useCallback(async () => {
    try {
      const response = await axiosInstance.get(
        `/practice/detail?boardID=${boardID}`
      );
      setBoardDetail(response.data);
    } catch (error) {
      console.error("게시글 상세 정보 가져오기 실패:", error);
    }
  }, [boardID]);

  useEffect(() => {
    fetchBoardDetail(); // 초기 로드 시 게시글 상세 정보 가져오기
  }, [fetchBoardDetail]);

  // 게시글 삭제
  const deleteBoard = async () => {
    try {
      await axiosInstance.get(
        `/practice/detail/delete?boardID=${boardID}`
      );
      alert("게시글이 삭제되었습니다!");
      handleListClick()
    } catch (error) {
      console.error("게시글 삭제 실패:", error);
    }
  };

  // 게시글 수정 버튼 클릭
  const handleUpdateClick = () => {
    navigate(`/Practice/PracticeUpdate/${boardID}`);
  };

  // 게시글 목록으로 이동
  const handleListClick = () => {
    navigate("/Practice");
  };


  if (!boardDetail) {
    return <div>게시글 정보를 불러오지 못했습니다.</div>;
  }

  // 댓글 생성
  const createComment = async () => {
    try {
      await axiosInstance.post("/practice/detail/commentWrite", {
        boardID: boardID,
        userID: currentUserID,
        content: content,
      });
      setContent("");
      fetchBoardDetail();
    } catch (error) {
      console.error("댓글 작성 실패:", error);
    }
  };

  // 댓글 삭제
  const deleteComment = async (commentID) => {
    try {
      await axiosInstance.get(
        `/practice/detail/commentDelete?commentID=${commentID}`
      );
      alert("댓글이 삭제되었습니다!");
      fetchBoardDetail();
    } catch (error) {
      console.error("댓글 삭제 실패:", error);
    }
  };

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
                        <a
                          href={`http://localhost:8090/practice/download?filePath=${encodeURIComponent(
                            file.storedPath
                          )}`}
                          download
                        >
                          다운로드
                        </a>
                      </p>
                    ))}
                  </div>
                </td>
              </tr>
            )}
            {(String(boardDetail.writerID) === String(currentUserID) || role === "ROLE_ADMIN") ? (
                <tr>
                  <td colSpan="3" className="board-buttons">
                    <button
                      onClick={handleListClick}
                      className="action-button list-button"
                      type="button"
                    >
                      목록
                    </button>
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
                  </td>
                </tr>
              ) : (
                <tr>
                  <td colSpan="3" className="board-buttons">
                    <button
                      onClick={handleListClick}
                      className="action-button list-button"
                      type="button"
                    >
                      목록
                    </button>
                  </td>
                </tr>
              )}
            <tr>
              <td className="label2">댓글</td>
              <td>
                <div className="board-comments">
                  {boardDetail.comments.map((comment, index) => (
                    <div key={index} className="comment-box">
                      <div className="comment-header">
                        <span className="comment-writer">
                          {comment.writerName}
                        </span>
                        <span className="comment-date">{comment.date}</span>
                      </div>
                      <div className="comment-content">{comment.content}</div>
                      {(String(comment.userID) === String(currentUserID) || decodedToken.role === "admin") && (
                          <button
                          className="button-commentDelete"
                          onClick={() => deleteComment(comment.commentID)}
                          >
                          삭제
                        </button>
                      )}
                    </div>
                  ))}
                </div>
                <div className="comment-form">
                  <textarea
                    placeholder="댓글을 작성하세요..."
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    className="comment-textarea"
                  />
                  <button
                    type="button"
                    onClick={createComment}
                    className="comment-submit-button"
                  >
                    댓글 작성
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </form>
    </div>
  );
};

export default PracticeBoardDetail;
