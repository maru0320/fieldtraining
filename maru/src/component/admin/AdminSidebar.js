import React from "react";
import "./Admin.css"; // 스타일 파일을 별도로 작성하여 import
import MemberManagement from "./MemberManagement";
import { Link } from "react-router-dom"; // Link 임포트

function AdminSidebar() {
  return (


        <div className="admin_sidebar">
          <ul>
            <li>
              <Link to="/admin/memberManagement">회원관리</Link>
            </li>
            <li>
                <Link to="/AdminMatches">매칭관리</Link>
            </li>
            <li>
              <p>홈페이지 관리</p>
            </li>
          </ul>
        </div>
  );
}

export default AdminSidebar;
