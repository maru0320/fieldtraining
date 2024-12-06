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
  const [userId,setUserId] = useState(null);
  const [isMatched, setIsMatched] = useState(false); // 매칭 여부 상태




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
          const response = await axiosInstance.get(`/matching/check/${studentId}`);
          setIsMatched(response.data.isMatched); // 매칭 여부에 따라 상태 변경
        } catch (err) {
          console.error("매칭 상태 확인 오류:", err);
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
        alert('로그아웃 중 오류가 발생했습니다.');
    }
};



const onClickMyActivity = async () => {
  try {
    const token = localStorage.getItem("token");
    const decodedToken = jwtDecode(token);

    const userId = decodedToken.userId;

    

      if (userRoles.includes("ROLE_STUDENT")){
      // 서버로 GET 요청 보내기 (Authorization 헤더에 Bearer 토큰 포함)
      const response = await axiosInstance.get(
        `/matching/status/${userId}`

      );
      // 서버 응답 성공 시 페이지 이동
      if (response.status === 200 || response.status === 201) {
        navigate("/studentpage");
      } else {
        alert("권한이 없습니다.");
      }
      } else if (userRoles.includes("ROLE_TEACHER")){
      const response = await axiosInstance.get(
        `/matching/teacher/${userId}/pending`
      );

      // 서버 응답 성공 시 페이지 이동
      if (response.status === 200 || response.status === 201) {
        navigate("/teacherpage");
      } else {
        alert("권한이 없습니다.");
      }
     }

    
  } catch (error) {
    // 오류 처리
    if (error.response) {
      // 서버가 응답은 했지만 오류가 있는 경우
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
      // 서버와의 연결 실패
      alert("서버와의 연결이 실패했습니다. 나중에 다시 시도해주세요.");
    } else {
      // 그 외의 오류 발생
      console.error("에러 발생:", error.message);
      alert("알 수 없는 오류가 발생했습니다.");
    }
  }
};
           

  const onClickAdminPage = () => {
    navigate("/admin"); // 관리자 페이지로 이동
};



  // 로고 클릭 시 메인 페이지로 이동
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
              <button onClick={onClickMyActivity}>나의 활동</button>
              {isMatched && <button onClick={handleAttendanceRequest}>출석</button>} {/* 매칭된 경우 출석 버튼 추가 */}
              <button onClick={onClickLogout}>로그아웃</button>
            </>
          ) : userRoles.includes("ROLE_STUDENT_MANAGER") || userRoles.includes("ROLE_COLLEGE_MANAGER") ? (
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