import React, { useState, useEffect } from 'react';
import logoImage from '../img/logo.jpg';
import './Header.css';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

function Header() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRoles, setUserRoles] = useState([]);
  const [userId, setUserId] = useState(null);
  const [isMatched, setIsMatched] = useState(false); // 매칭 여부 상태

  useEffect(() => {
    const handleStorageChange = () => {
      const token = localStorage.getItem("token");
      if (token) {
        try {
          const decodedToken = jwtDecode(token);
          const userId = decodedToken.sub;

          setUserId(userId);
          setIsLoggedIn(true);
          setUserRoles(decodedToken.roles || []);

          // 학생이 매칭된 상태인지 확인하는 API 호출
          checkIfMatched(userId);
        } catch (error) {
          console.error("JWT 디코딩 오류:", error);
          setIsLoggedIn(false);
          setUserRoles([]);
        }
      } else {
        setIsLoggedIn(false);
        setUserRoles([]);
      }
    };

    // 매칭 여부 확인 함수
    const checkIfMatched = async (studentId) => {
      try {
        const response = await axios.get(`http://localhost:8090/matching/check/${studentId}`);
        setIsMatched(response.data.isMatched); // 매칭 여부에 따라 상태 변경
      } catch (err) {
        console.error("매칭 상태 확인 오류:", err);
      }
    };

    // storage 이벤트 리스너 등록
    handleStorageChange(); // 컴포넌트가 마운트될 때 바로 한 번 호출
    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, []); // []로 빈 배열을 전달해 한 번만 실행되도록 설정

  // 출석 요청 함수
  const handleAttendanceRequest = async () => {
    try {
      const response = await axios.post("http://localhost:8090/attendance/request", {
        studentId: userId,
      });

      if (response.status === 200) {
        alert("출석 요청이 완료되었습니다.");
      }
    } catch (err) {
      console.error("출석 요청 오류", err);
      alert("출석 요청에 실패했습니다.");
    }
  };

  // 로그인 버튼 클릭 시
  const onClickLogin = () => {
    navigate("/login");
  };

  // 회원가입 버튼 클릭 시
  const onClickJoin = () => {
    navigate("/joinselect");
  };

  const onClickLogout = async (event) => {
    event.preventDefault();

    try {
      const response = await axios.post(
        'http://localhost:8090/api/auth/logout',
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
          },
        }
      );

      if (response.status === 200) {
        localStorage.removeItem('token');
        alert('로그아웃 성공');
        setIsLoggedIn(false);
        setUserRoles([]);
        navigate("/login");
      } else {
        alert('로그아웃 실패');
      }
    } catch (error) {
      console.error('네트워크 오류:', error);
      alert('로그아웃 중 오류가 발생했습니다.');
    }
  };

  const onClickMyActivity = () => {
    navigate("/myactivity");
  };

  const onClickAdminPage = () => {
    navigate("/admin");
  };

  const onClickLogo = () => {
    navigate("/");
  };

  return (
    <div className="banner-header">
      <img 
        src={logoImage} 
        alt="Logo" 
        className="logo-image" 
        onClick={onClickLogo} 
      />
      <nav className="banner-nav">
        {/* 기존 네비게이션 */}
      </nav>
      <div className="auth-buttons">
        {isLoggedIn && userRoles.length > 0 ? (
          userRoles.includes("STUDENT") || userRoles.includes("PROFESSOR") || userRoles.includes("TEACHER") ? (
            <>
              <button onClick={onClickMyActivity}>나의 활동</button>
              {isMatched && <button onClick={handleAttendanceRequest}>출석</button>} {/* 매칭된 경우 출석 버튼 추가 */}
              <button onClick={onClickLogout}>로그아웃</button>
            </>
          ) : userRoles.includes("STUDENT_MANAGER") || userRoles.includes("COLLEGE_MANAGER") ? (
            <>
              <button onClick={onClickAdminPage}>관리자 페이지</button>
              <button onClick={onClickLogout}>로그아웃</button>
            </>
          ) : (
            <div>권한 없음</div>
          )
        ) : (
          <>
            <button onClick={onClickLogin}>로그인</button>
            <button onClick={onClickJoin}>회원가입</button>
          </>
        )}
      </div>
    </div>
  );
}

export default Header;
