import React, { useState } from "react";
import UploadBox from "../../UploadBox";
import axios from "axios";
import '../../write.css';
import { useNavigate } from "react-router-dom";

function NoticeWrite() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [file, setFile] = useState(null);

  const handleRegister = async () => {
    const postData = {
      title,
      content,
      writer: "작성자 이름",
      date: new Date().toISOString(),
      file: file, // 파일 객체
    };
  
    try {
      const formData = new FormData();
      formData.append("file", file);
      
      // 실제 POST 요청을 보내기 전에 JSON 형태로 데이터를 보낼 수 있도록 수정
      await axios.post("http://localhost:8090/api/notice", postData, {
        headers: {
          "Content-Type": "application/json", // JSON 요청을 명시
        },
      });
      navigate("/Notice");
    } catch (error) {
      console.error("게시글 등록 오류:", error);
    }
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  return (
  <div className="container">  
  <h2>등록</h2>
    <div className="Write">
      
      <table className="enroll-table">
        <thead>
          <tr>
            <th>제목</th>
            <td colSpan={3}>
              <input
                type="text"
                name="글제목"
                onChange={(e) => setTitle(e.target.value)}
              />
            </td>
          </tr>
          <tr className="upload">
            <th>실습파일</th>
                <td>
                <UploadBox onFileChange={handleFileChange}/>
                </td>
          </tr>
          <tr>
            <th>글내용</th>
            <td colSpan={3} style={{ height: "200px" }}>
              <textarea
                className="detail"
                onChange={(e) => setContent(e.target.value)}
              ></textarea>
            </td>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th colSpan={4}>
              <button onClick={handleRegister}>등록</button>
            </th>
          </tr>
        </tbody>
      </table>
    </div>
</div>
  );
}

export default NoticeWrite;
