import React, { useState, useEffect } from "react";
import { jwtDecode } from 'jwt-decode';
import "./Mypage.css"; // CSS 파일 import
import Sidebar from "../../Sidebar";
import axiosInstance from "../../axiosInstance";

const MyPage = () => {
  const token = localStorage.getItem("token"); // 로컬 스토리지에서 JWT 토큰 가져오기
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  // 토큰에서 사용자 ID 디코딩
  const decodedToken = jwtDecode(token);
  const userId = decodedToken.userId;

  // 유저 정보 가져오기
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axiosInstance.get(`/mypage/userinfo?id=${userId}`);
        setUserInfo(response.data);
      } catch (err) {
        console.error(err);
        setError("유저 정보를 가져오지 못했습니다.");
      }
    };

    fetchUserInfo();
  },[]);

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
                <span className="user-info-value">{userInfo.studentNumber}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">전공:</span>
                <span className="user-info-value">{userInfo.department}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">대학교:</span>
                <span className="user-info-value">{userInfo.college}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">실습학교:</span>
                <span className="user-info-value">{userInfo.schoolName}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">과목:</span>
                <span className="user-info-value">{userInfo.subject}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">이메일:</span>
                <span className="user-info-value">{userInfo.email}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">핸드폰번호:</span>
                <span className="user-info-value">{userInfo.phoneNumber}</span>
              </div>
            </>
          )}

          {/* 교사 정보 */}
          {userInfo.role === "teacher" && (
            <>
              <h2 className="role-title">담당교사</h2>
              <div className="user-info">
                <span className="user-info-label">이름:</span>
                <span className="user-info-value">{userInfo.name}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">소속학교:</span>
                <span className="user-info-value">{userInfo.schoolName}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">사무실번호:</span>
                <span className="user-info-value">{userInfo.officeNumber}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">담당과목:</span>
                <span className="user-info-value">{userInfo.subject}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">이메일:</span>
                <span className="user-info-value">{userInfo.email}</span>
              </div>
              <div className="user-info">
                <span className="user-info-label">핸드폰번호:</span>
                <span className="user-info-value">{userInfo.phoneNumber}</span>
              </div>
            </>
          )}
        </div>
      </form>
    </div>
  );
};

export default MyPage;
