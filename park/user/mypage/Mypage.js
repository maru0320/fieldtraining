import React, { useState, useEffect } from "react";
import { jwtDecode } from 'jwt-decode';
import "./Mypage.css"; // CSS 파일 import
import Sidebar from "../../Sidebar";
import axiosInstance from "../../axiosInstance";

const MyPage = () => {
  const token = localStorage.getItem("token"); // 로컬 스토리지에서 JWT 토큰 가져오기
  const [userInfo, setUserInfo] = useState(null);
  const [editedInfo, setEditedInfo] = useState(null);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  // 토큰에서 사용자 ID 디코딩
  const decodedToken = jwtDecode(token);
  const userId = decodedToken.userId;

  // 유저 정보 가져오기
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axiosInstance.get(`/mypage/userinfo?id=${userId}`);
        setUserInfo(response.data);
        setEditedInfo(response.data);
      } catch (err) {
        console.error(err);
        setError("유저 정보를 가져오지 못했습니다.");
      }
    };

    fetchUserInfo();
  },[userId]);

  const handleChange = (field, value) => {
    setEditedInfo((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleSave = async () => {
    try {
      await axiosInstance.post("/mypage/update", {
        ...editedInfo,
        id: userId,
      });
      setUserInfo(editedInfo);
      setIsEditing(false);
      alert("정보가 성공적으로 업데이트되었습니다.");
    } catch (err) {
      console.error(err);
      alert("정보 업데이트에 실패했습니다.");
    }
  };

  if (error) return <div>{error}</div>;
  if (!userInfo) return <div>로딩 중...</div>;

  return (
    <div className="mypage-body">
      {/* 사이드바 */}
      <div className="sidebar-container">
        <Sidebar />
      </div>
      {/* 마이페이지 메인 콘텐츠 */}
      <form className="mypage-container">
        <h1 className="mypage-title">마이페이지</h1>
        <div className="role-section">
          {/* 학생 정보 */}
          {userInfo.role === "student" && (
            <>
              <h2 className="role-title">교육실습생</h2>
              <div className="user-info">
                <span className="user-info-label">이름:</span>
                
                  <span className="user-info-value">{userInfo.name}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">학번:</span>
                {isEditing ? (
                  <input
                    type="number"
                    value={editedInfo.studentNumber}
                    onChange={(e) => handleChange("studentNumber", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.studentNumber}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">전공:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.department}
                    onChange={(e) => handleChange("department", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.department}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">대학교:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.college}
                    onChange={(e) => handleChange("college", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.college}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">실습학교:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.schoolName}
                    onChange={(e) => handleChange("schoolName", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.schoolName}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">과목:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.subject}
                    onChange={(e) => handleChange("subject", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.subject}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">이메일:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.email}
                    onChange={(e) => handleChange("email", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.email}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">핸드폰번호:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.phoneNumber}
                    onChange={(e) => handleChange("phoneNumber", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.phoneNumber}</span>
                )}
              </div>
            </>
          )}

          {/* 교사 정보 */}
          {userInfo.role === "teacher" && (
            <>
              <h2 className="role-title">담당교사</h2>
              <div className="user-info">
                <span className="user-info-label">이름:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.name}
                    onChange={(e) => handleChange("name", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.name}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">소속학교:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.schoolName}
                    onChange={(e) => handleChange("schoolName", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.schoolName}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">사무실번호:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.officeNumber}
                    onChange={(e) => handleChange("officeNumber", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.officeNumber}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">담당과목:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.subject}
                    onChange={(e) => handleChange("subject", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.subject}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">이메일:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.email}
                    onChange={(e) => handleChange("email", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.email}</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-info-label">핸드폰번호:</span>
                {isEditing ? (
                  <input
                    type="text"
                    value={editedInfo.phoneNumber}
                    onChange={(e) => handleChange("phoneNumber", e.target.value)}
                  />
                ) : (
                  <span className="user-info-value">{userInfo.phoneNumber}</span>
                )}
              </div>
            </>
          )}
        </div>
         {/* 버튼 */}
         <div className="action-buttons">
          {isEditing ? (
            <>
              <button type="button" onClick={handleSave}>
                저장
              </button>
              <button type="button" onClick={() => setIsEditing(false)}>
                취소
              </button>
            </>
          ) : (
            <button type="button" onClick={() => setIsEditing(true)}>
              수정
            </button>
          )}
        </div>
      </form>
    </div>
  );
};

export default MyPage;
