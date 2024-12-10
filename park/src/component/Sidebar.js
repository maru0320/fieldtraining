import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./Sidebar.css";
import axiosInstance from "./axiosInstance";

const Sidebar = () => {
  const [isMatchingAllowed, setIsMatchingAllowed] = useState(false); // Spring Boot 응답 값 저장
  const token = localStorage.getItem("token"); // 토큰 가져오기
  const decoded = jwtDecode(token);
  const userId = decoded.userId;
  const userRole = decoded.roles;

  useEffect(() => {
    if (token) {


      // Spring Boot API 호출
      axiosInstance
        .get(`/matching/check/${userId}`)
        .then((response) => {
          console.log(response.data)
          setIsMatchingAllowed(response.data); // 서버에서 true/false 값 받음
        })
        .catch((error) => {
          console.error("API 호출 오류:", error);
        });
    }
  }, [token]);

  // 클릭 이벤트 처리
  const handleRestrictedClick = () => {
    alert("매칭부터 하십시오.");
  };

  return (
    <div className="sidebar">
      <ul>
        
        <li>
          <Link to="/mypage">내정보</Link>
        </li>
      
        {isMatchingAllowed.isMatched ? (
          <ul>
            <li>
              <Link to="/MatchInfo">매칭</Link>              
            </li>
            <li>
              <Link to="/practice">실습일지</Link>
            </li>
            <li>
              <Link to="/class">수업일지</Link>
            </li>
            <li>
              <Link to="/operation">학급운영일지</Link>
            </li>
            <li>
              <Link to="/union">교직실무일지</Link>
            </li>
            <li>
              <Link to="/rating">평가</Link>
            </li>
            <li>
              {userRole.includes("ROLE_TEACHER") ? (
                <>
                <Link to="/TeacherAttendanceApproval">출석승인</Link>
                <br/>
                <Link to="/TeacherMatchedStudentsAttendancePage">출석현황</Link>
                </>
              ):(<Link to="/AttendanceStatus">출석현황</Link>)}
            </li>
            <li>
              <Link to="/mypage">일정</Link>
            </li>
          </ul>
        ) : (
          <ul>
            <li>
              {userRole.includes("ROLE_STUDENT") ? (
                <Link to="/studentPage">매칭</Link>
              ):(<Link to="/teacherPage">매칭</Link>)}
              
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                실습일지
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                수업일지
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                학급운영일지
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                교직실무일지
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                평가
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                출석
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                출석현황
              </button>
            </li>
            <li>
              <button onClick={handleRestrictedClick} className="disabled-link">
                일정
              </button>
            </li>
          </ul>
        )}
      </ul>
    </div>
  );
};

export default Sidebar;
