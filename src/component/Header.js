import React from 'react';
import logoImage from '../img/logo.jpg';
import './Header.css';
import { useNavigate } from "react-router-dom";

function Header() {
  const navigate = useNavigate();

  // 로그인 버튼 클릭 시
  const onClickLogin = () => {
      navigate("/login");
  };

  // 회원가입 버튼 클릭 시
  const onClickJoin = () => {
      navigate("/joinselect");
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
        <button className="auth-button" onClick={onClickLogin}>로그인</button>
        <button className="auth-button" onClick={onClickJoin}>회원가입</button>
      </div>
    </div>
  );
}

export default Header;