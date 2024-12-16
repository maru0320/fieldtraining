import React, { useState, useRef } from "react";
import "./ReferenceWrite.css"; // 스타일링을 위한 CSS 파일 추가
import { useNavigate } from "react-router-dom"; // useNavigate 임포트
import { jwtDecode } from 'jwt-decode';
import axiosInstance from "../../axiosInstance";


const ReferenceWrite = () => {
  const token = localStorage.getItem("token");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [fileList, setFileList] = useState([]); // 파일 리스트를 관리
  const fileInputRef = useRef(null); // 숨겨진 파일 입력 필드를 참조
  const navigate = useNavigate(); // navigate 함수 사용
  // 파일 추가 버튼 클릭 시 파일 선택 트리거
  const handleAddFile = () => {
    fileInputRef.current.click(); // 숨겨진 input을 클릭
  };
  // 파일 선택 시 파일 리스트에 추가
  const handleFileChange = (e) => {
    const selectedFiles = Array.from(e.target.files); // 새로 선택한 파일들
    setFileList((prevList) => [...prevList, ...selectedFiles]); // 기존 파일과 합치기
  };
  
  // 업로드 처리
  const handleSubmit = async (e) => {
    e.preventDefault();

    const decodedToken = jwtDecode(token);
    console.log("Decoded Token:", decodedToken);
    const sub = decodedToken.userId;
    const formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    formData.append("writerID", sub); // 임시로 작성자 ID를 1로 설정
    fileList.forEach((file) => {
      formData.append("files", file); // 'files'로 key를 설정
    });
    try {
      const response = await axiosInstance.post("/reference/write", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("글 작성 성공:", response.data);
      alert("게시글이 작성 되었습니다!")
      navigate("/reference"); 
    } catch (error) {
      console.error("글 작성 실패:", error);
    }
  };
  
  return (
    <div className="write-container">
      <h1 className="write-title">게시글 작성</h1>
      <form onSubmit={handleSubmit} className="write-form">
        <input
          type="text"
          placeholder="제목을 입력하세요"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="write-input"
        />
        <textarea
          placeholder="내용을 입력하세요"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className="write-textarea"
        />
        {/* 숨겨진 파일 입력 */}
        <input
          type="file"
          ref={fileInputRef} // 참조 연결
          multiple
          style={{ display: "none" }} // 화면에 보이지 않음
          onChange={handleFileChange}
        />
        {/* 파일 추가 버튼 */}
        <button type="button" onClick={handleAddFile} className="file-add-button">
          파일 추가
        </button>
        {/* 선택된 파일 리스트 보여주기 */}
        <ul className="file-list">
          {fileList.map((file, index) => (
            <li key={index} className="file-item">
              {file.name}
            </li>
          ))}
        </ul>
        <button type="submit" className="submit-button">작성</button>
      </form>
    </div>
  );
};
export default ReferenceWrite;