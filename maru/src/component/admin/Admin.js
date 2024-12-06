import React from "react";
import "./Admin.css"; // 스타일 파일을 별도로 작성하여 import
import MemberManagement from "./MemberManagement";
import { Link } from "react-router-dom"; // Link 임포트

function Admin() {
  return (
    <div className="admin-page">
      {/* 헤더 */}
      <div className="header">
        <h1>관리자 페이지</h1>
      </div>

      {/* 본문 영역 */}
      <div className="content-wrapper">
        {/* 사이드바 */}
        <div className="admin_sidebar">
          <ul>
            <li>
              <Link to="/admin/memberManagement">회원관리</Link>
            </li>
            <li>
              <p>매칭 현황</p>
            </li>
            <li>
              <p>홈페이지 관리</p>
            </li>
          </ul>
        </div>

        {/* 메인 콘텐츠 */}
        <div className="main_content">
          
        </div>
      </div>


    </div>
  );
}

export default Admin;
