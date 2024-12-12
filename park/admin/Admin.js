import React from "react";
import "./Admin.css"; // 스타일 파일을 별도로 작성하여 import
import { Link } from "react-router-dom"; // Link 임포트
import Sidebar from "../Sidebar";

function Admin() {
  return (
    <div className="admin-page">

     
        {/* 사이드바 */}
        <Sidebar/>
        {/* 메인 콘텐츠 */}
       


    </div>
  );
}

export default Admin;
