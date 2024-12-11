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
  const [id,setUserId] = useState([null]);

  useEffect(() => {
    const handleStorageChange = () => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                console.log("Decoded Token:", decodedToken);
                const userId = decodedToken.userId;
                const sub = decodedToken.sub;

                setUserId(id);
                setIsLoggedIn(true);
                setUserRoles(decodedToken.roles || []); // roles 배열 설정

                console.log("User ID:", userId);
                console.log("Long ID:", sub);
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

    // 초기 상태 설정
    handleStorageChange();

    // storage 이벤트 리스너 등록
    window.addEventListener("storage", handleStorageChange);

    // 컴포넌트 언마운트 시 리스너 제거
    return () => {
        window.removeEventListener("storage", handleStorageChange);
    };
}, []);





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
      navigate("/myactivity"); // "나의 활동" 페이지로 이동
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
                        userRoles.includes("STUDENT") || userRoles.includes("PROFESSOR") || userRoles.includes("TEACHER") ? (
                            <>
                                <button onClick={onClickMyActivity}>나의 활동</button>
                                <button onClick={onClickLogout}>로그아웃</button>
                            </>
                        ) : userRoles.includes("SCHOOL_MANAGER") || userRoles.includes("COLLEGE_MANAGER") ? (
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