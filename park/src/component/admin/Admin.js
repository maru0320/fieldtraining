import React from "react";
import "./Admin.css"; // 스타일 파일을 별도로 작성하여 import
import { Link } from "react-router-dom"; // Link 임포트
import AdminSidebar from "./AdminSidebar";

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
        <AdminSidebar />

        {/* 메인 콘텐츠 */}
        <div className="main_content">
          
        </div>
      </div>


    </div>
  );
}

export default Admin;
