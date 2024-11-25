import React, { useState } from "react";
import UploadBox from "../../UploadBox";
import '../../write.css';
import { useNavigate } from "react-router-dom";

function ReferenceWrite({ postArray, setPostArray }) {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [detail, setDetail] = useState('');

  // 게시글 등록
  const regist = () => {
    // 새로운 게시글 객체 생성
    const maxId = postArray.length > 0 ? Math.max(...postArray.map(post => post.id)) : 0;
    const post = {
      id: maxId + 1, // 가장 큰 ID 값에 1을 더해서 새로운 ID를 생성
      title, 
      detail, 
      date: new Date().toLocaleDateString(), // 현재 날짜
    };
  
    // 게시글 배열에 추가
    setPostArray([post, ...postArray]);
    // 등록 후 FaQBorad 페이지로 이동
    navigate("/ReferenceBoard");
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
                <UploadBox />
                </td>
          </tr>
          <tr>
            <th>글내용</th>
            <td colSpan={3} style={{ height: "200px" }}>
              <textarea
                className="detail"
                onChange={(e) => setDetail(e.target.value)}
              ></textarea>
            </td>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th colSpan={4}>
              <button onClick={regist}>등록</button>
            </th>
          </tr>
        </tbody>
      </table>
    </div>
</div>
  );
}

export default ReferenceWrite;
