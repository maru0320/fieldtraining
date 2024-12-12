import React, { useState, useEffect } from 'react';
import logoImage from '../img/logo.jpg';
import './Header.css';
import { useNavigate } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';
import axiosInstance from './axiosInstance';

function Header() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRoles, setUserRoles] = useState([]);
  const [userId, setUserId] = useState(null);
  const [isMatched, setIsMatched] = useState(false); // 매칭 여부 상태
  const [isApproval, setIsApproval] = useState(false);
  const [hasRequestedAttendance, setHasRequestedAttendance] = useState(false); // 출석 요청 여부 상태

  useEffect(() => {
    const handleStorageChange = () => {
      const token = localStorage.getItem("token");
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        const userId = decodedToken.userId;

        setUserId(userId);
        setIsLoggedIn(true);
        setUserRoles(decodedToken.roles || []);

        console.log("역할 :", decodedToken.roles);

        // 역할에 따라 필요한 상태만 확인
        if (decodedToken.roles.includes("ROLE_STUDENT") || decodedToken.roles.includes("ROLE_PROFESSOR") || decodedToken.roles.includes("ROLE_TEACHER")) {
          checkIfMatched(userId);
          checkAttendanceRequest(userId);
          checkIsApproval(userId);
        }
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
        const response = await axiosInstance.get(`/matching/checked/${studentId}`);
        setIsMatched(response.data.isMatched); // 매칭 여부에 따라 상태 변경
      } catch (err) {
        console.error("매칭 상태 확인 오류:", err);
      }
    };

    const checkIsApproval = async (userId) => {
      try {
        const response = await axiosInstance.get(`/isapproval/check/${userId}`);
        setIsApproval(response.data.isApproval);
      } catch (err) {
        console.error("승인 확인 오류", err);
      }
    };

    // 출석 요청 여부 확인 함수
    const checkAttendanceRequest = async (studentId) => {
      try {
        const response = await axiosInstance.get(`/attendance/check/${studentId}`);
        setHasRequestedAttendance(response.data.hasRequested); // 오늘 출석 요청 여부
      } catch (err) {
        console.error("출석 요청 상태 확인 오류", err);
      }
    };

    // 초기 상태 설정
    handleStorageChange();

    // storage 이벤트 리스너 등록
    window.addEventListener("storage", handleStorageChange);

    // 컴포넌트 언마운트 시 리스너 제거
    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, []);

  // 출석 요청 함수
  const handleAttendanceRequest = async () => {
    const token = localStorage.getItem("token");
    const decodedToken = jwtDecode(token);
    const userId = decodedToken.userId;
    try {
      const response = await axiosInstance.post("/attendance/request", {
        studentId: userId,
      });

      if (response.status === 200) {
        setHasRequestedAttendance(true); // 출석 요청 후 상태 갱신
        alert("출석 요청이 완료되었습니다.");
      }
    } catch (err) {
      console.error("출석 요청 오류", err);
      alert("오늘은 출석요청이 완료되었습니다.");
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
      const response = await axiosInstance.post(
        '/api/auth/logout',
        {}, {
          headers: {
            'Content-Type': 'application/json'
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
      alert('로그인 시간이 만료되었습니다.');
      localStorage.removeItem('token');
      setIsLoggedIn(false);
      setUserRoles([]);
      navigate("/login");
    }
  };

  const onClickMyActivity = async () => {
    try {
      const token = localStorage.getItem("token");
      const decodedToken = jwtDecode(token);
      const userId = decodedToken.userId;

      if (userId !== null) {
        const response = await axiosInstance.get(
          `/mypage/userinfo?id=${userId}`
        );
        if (response.status === 200 || response.status === 201) {
          navigate("/mypage");
        } else {
          alert("권한이 없습니다.");
        }
      }
    } catch (error) {
      if (error.response) {
        switch (error.response.status) {
          case 401:
            alert("인증에 실패했습니다. 다시 로그인하세요.");
            navigate("/");
            break;
          case 403:
            alert("접근 권한이 없습니다.");
            navigate("/");
            break;
          default:
            alert("알 수 없는 오류가 발생했습니다.");
        }
      } else if (error.request) {
        alert("서버와의 연결이 실패했습니다. 나중에 다시 시도해주세요.");
      } else {
        console.error("에러 발생:", error.message);
        alert("알 수 없는 오류가 발생했습니다.");
      }
    }
  };

  const onClickManagerPage = () => {
    navigate("/ManagerInfo"); // 관리자 페이지로 이동
  };

  const onClickAdminPage = () => {
    navigate("/admin"); // 관리자 페이지로 이동
  };

  const onClickLogo = () => {
    navigate("/");  // "/" 경로는 메인 페이지입니다.
  };

  return (
    <div className="banner-header">
      <img 
        src={logoImage} 
        alt="Logo" 
        className="logo-image" 
        onClick={onClickLogo} // 로고 클릭 시 메인 화면으로 이동
      />
      <nav className="banner-nav">
        <ul className="banner-nav-list">
          <li className="banner-nav-item">
            <span className="banner-text">현장실습학기제</span>
            <ul className="dropdown-menu">
              <li className="dropdown-item">현장실습학기제란?</li>
              <li className="dropdown-item">운영 모형</li>
              <li className="dropdown-item">한 눈에 보는 현장실습학기제</li>
            </ul>
          </li>
          <li className="banner-nav-item">
            <span className="banner-text">중앙지원센터</span>
            <ul className="dropdown-menu">
              <li className="dropdown-item">중앙지원센터 소개</li>
              <li className="dropdown-item">중앙지원센터 사업</li>
            </ul>
          </li>
          <li className="banner-nav-item">
            <span className="banner-text">자료실</span>
            <ul className="dropdown-menu">
              <li className="dropdown-item">매뉴얼/서식</li>
              <li className="dropdown-item">운영 사례</li>
              <li className="dropdown-item">영상자료</li>
              <li className="dropdown-item">기타자료</li>
            </ul>
          </li>
          <li className="banner-nav-item">
            <span className="banner-text">정보마당</span>
            <ul className="dropdown-menu">
              <li className="dropdown-item">공지사항</li>
              <li className="dropdown-item">갤러리</li>
            </ul>
          </li>
          <li className="banner-nav-item">
            <span className="banner-text">게시판</span>
            <ul className="dropdown-menu">
              <li className="dropdown-item">양식</li>
              <li className="dropdown-item">참고자료</li>
              <li className="dropdown-item">F&A 자주 묻는 질문</li>
              <li className="dropdown-item">F&A 시스템 건의사항</li>
            </ul>
          </li>
        </ul>
      </nav>
      <div className="auth-buttons">
        {isLoggedIn && userRoles.length > 0 ? (
          userRoles.includes("ROLE_STUDENT") || userRoles.includes("ROLE_PROFESSOR") || userRoles.includes("ROLE_TEACHER") ? (
            <>
              {isApproval ? (
                <>
                  <button onClick={onClickMyActivity}>나의 활동</button>
                  {isMatched && !hasRequestedAttendance && (
                    <button onClick={handleAttendanceRequest}>출석</button> // 출석 요청 버튼
                  )}
                  {hasRequestedAttendance && (
                    <button disabled>출석 요청 완료</button> // 이미 출석 요청이 완료된 경우
                  )}
                </>
              ) : (
                <p>승인 대기 중</p> // 승인 대기 중 표시
              )}
              <button onClick={onClickLogout}>로그아웃</button>
            </>
          ) : userRoles.includes("ROLE_SCHOOL_MANAGER") || userRoles.includes("ROLE_COLLEGE_MANAGER") ? (
            <>
              <button onClick={onClickManagerPage}>관리자 페이지</button>
              <button onClick={onClickLogout}>로그아웃</button>
            </>
          ) : userRoles.includes("ROLE_ADMIN") ? (
            <>
              <button onClick={onClickAdminPage}>페이지 관리</button>
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
