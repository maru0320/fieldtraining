import React, { useState, useEffect, useRef, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./UnionWrite.css";
import axiosInstance from "../../axiosInstance";

const UnionUpdate = () => {
  const navigate = useNavigate();
  const { boardID } = useParams();

  const [boardDetail, setBoardDetail] = useState(null);
  const [fileList, setFileList] = useState([]); // 새로 추가한 파일 리스트
  const fileInputRef = useRef(null); // 숨겨진 파일 입력 필드 참조
  const [loading, setLoading] = useState(true);

  // 게시글 상세정보 가져오기
  const fetchBoardDetail = useCallback(async () => {
    try {
      const response = await axiosInstance.get(`/union/detail?boardID=${boardID}`);
      setBoardDetail(response.data);
    } catch (error) {
      console.error("게시글 상세 정보 가져오기 실패:", error);
    } finally {
      setLoading(false);
    }
  }, [boardID]);

  useEffect(() => {
    fetchBoardDetail();
  }, [fetchBoardDetail]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!boardDetail) {
    return <div>게시글 정보를 불러오지 못했습니다.</div>;
  }

  // 글 수정
  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("boardID", boardID);
    formData.append("title", boardDetail.title || "");
    formData.append("content", boardDetail.content || "");

    // 새로 추가된 파일 처리
    fileList.forEach((file) => {
      formData.append("files", file);
    });

    try {
      await axiosInstance.post("/union/update", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      alert("게시글이 수정되었습니다!");
      navigate("/union");
    } catch (error) {
      console.error("글 수정 실패:", error);
    }
  };

  // 파일 삭제
  const handleFileDelete = async (fileID) => {
    try {
      await axiosInstance.get(`/union/update/fileDelete?fileID=${fileID}`);
      alert("파일이 삭제되었습니다.");
      setBoardDetail((prevDetail) => ({
        ...prevDetail,
        files: prevDetail.files.filter((file) => file.id !== fileID),
      }));
    } catch (error) {
      console.error("파일 삭제 실패:", error);
    }
  };

  // 새 파일 추가 버튼 클릭
  const handleAddFile = () => {
    fileInputRef.current.click();
  };

  // 새 파일 선택
  const handleFileChange = (e) => {
    const selectedFiles = Array.from(e.target.files);
    setFileList((prevList) => [...prevList, ...selectedFiles]);
  };

  return (
    <div className="write-container">
      <h1 className="write-title">게시글 수정</h1>
      <form onSubmit={handleSubmit} className="write-form">
        <input
          type="text"
          placeholder="제목을 입력하세요"
          value={boardDetail.title || ""}
          onChange={(e) => setBoardDetail({ ...boardDetail, title: e.target.value })}
          className="write-input"
        />
        <textarea
          placeholder="내용을 입력하세요"
          value={boardDetail.content || ""}
          onChange={(e) => setBoardDetail({ ...boardDetail, content: e.target.value })}
          className="write-textarea"
        />
        {/* 기존 파일 리스트 */}
        <div>
          <h3>첨부된 파일</h3>
          {boardDetail.files &&
            boardDetail.files.map((file) => (
              <div key={file.id}>
                <p>파일 이름: {file.originalName}</p>
                <button type="button" onClick={() => handleFileDelete(file.id)}>
                  삭제
                </button>
              </div>
            ))}
        </div>
        {/* 새 파일 추가 */}
        <div>
          <h3>새로 추가할 파일</h3>
          <input
            type="file"
            ref={fileInputRef}
            multiple
            style={{ display: "none" }}
            onChange={handleFileChange}
          />
          <button type="button" onClick={handleAddFile}>
            파일 추가
          </button>
          <ul>
            {fileList.map((file, index) => (
              <li key={index}>{file.name}</li>
            ))}
          </ul>
        </div>
        <button type="submit" className="submit-button">
          수정
        </button>
      </form>
    </div>
  );
};

export default UnionUpdate;
