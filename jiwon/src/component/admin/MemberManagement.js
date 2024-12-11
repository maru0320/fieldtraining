import React, { useEffect, useState } from "react";
import axiosInstance from "../axiosInstance";
import "./MemberManagement.css"; // 
import Sidebar from "../Sidebar";

const AdminUserManagement = () => {
  const [users, setUsers] = useState([]); // users 초기값을 빈 배열로 설정
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    axiosInstance
      .get("/admin/users")
      .then((response) => {
        console.log("API 응답:", response.data); // 응답 구조 확인
        // 응답이 배열인지 확인하고 배열로 처리
        setUsers(Array.isArray(response.data) ? response.data : []);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching users:", error);
        setError("회원 데이터를 불러오는 데 실패했습니다.");
        setLoading(false);
      });
  }, []);

  // 승인/거부 처리
  const handleApproval = (userId, isApproved) => {
    axiosInstance
      .put(`/admin/users/${userId}/approval`, null, { params: { isApproved } })
      .then(() => {
        setUsers((prevUsers) =>
          prevUsers.map((user) =>
            user.id === userId ? { ...user, approval: isApproved } : user
          )
        );
      })
      .catch((error) =>
        console.error("Error updating approval status:", error)
      );
  };

  // 삭제 처리
  const handleDelete = (userId) => {
    axiosInstance
      .delete(`/admin/users/${userId}`)
      .then(() => {
        setUsers((prevUsers) => prevUsers.filter((user) => user.id !== userId));
      })
      .catch((error) => console.error("Error deleting user:", error));
  };

  // 로딩 상태
  if (loading) {
    return <div>로딩 중...</div>;
  }

  // 에러 상태
  if (error) {
    return <div>{error}</div>;
  }

  // 역할별로 구분하여 보여줄 테이블
  const renderUserDetails = (user) => {
    if (user.role === "student" && user.studentDetail) {
      return (
        <div>
          <p>이름: {user.studentDetail.name} 학과: {user.studentDetail.department} 학번: {user.studentDetail.studentNumber}</p>
        </div>
      );
    } else if (user.role === "teacher" && user.teacherDetail) {
      return (
        <div>
          <p>이름: {user.teacherDetail.name} 과목: {user.teacherDetail.subject}</p>
        </div>
      );
    } else if (user.role === "professor" && user.professorDetail) {
      return (
        <div>
          <p>이름: {user.professorDetail.name} 학과: {user.professorDetail.department}</p>
        </div>
      );
    } else if (user.role === "collegeManager" && user.collegeManagerDetail) {
      return (
        <div>
          <p>기관: {user.collegeManagerDetail.college} 담당자ID: {user.schoolManagerDetail.managerId} 사무실번호: {user.schoolManagerDetail.officeNumber}</p>
        </div>
      );
    } else if (user.role === "schoolManager" && user.schoolManagerDetail) {
      return (
        <div>
          <p>학교: {user.schoolManagerDetail.schoolName} 담당자ID: {user.schoolManagerDetail.managerId} 사무실번호: {user.schoolManagerDetail.officeNumber}</p>
        </div>
      );
    }else {
      return <p>세부 정보 없음</p>;
    }
  };

  return (
    <div className="mypage-body">
      <div className="sidebar-container">
        <Sidebar />
      </div>
      <div className="adminMember-container"> 
      <h1>회원 관리</h1>
      {/* 학생 목록 */}
      <h2>학생</h2>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>세부 정보</th>
            <th>승인 여부</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {users
            .filter((user) => user.role === "student")
            .map((user) => (
              <tr key={user.id}>
                <td>{user.userId}</td>
                <td>{renderUserDetails(user)}</td>
                <td>{user.approval ? "승인됨" : "미승인"}</td>
                <td>
                  {user.approval ? (
                    <p>승인됨</p>
                    ) : (
                    <button onClick={() => handleApproval(user.id, true)}>승인</button>
                    )}
                  <button onClick={() => handleApproval(user.id, false)}>거부</button>
                  <button onClick={() => handleDelete(user.id)}>삭제</button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>

      {/* 교사 목록 */}
      <h2>교사</h2>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>세부 정보</th>
            <th>승인 여부</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {users
            .filter((user) => user.role === "teacher")
            .map((user) => (
              <tr key={user.id}>
                <td>{user.userId}</td>
                <td>{renderUserDetails(user)}</td>
                <td>{user.approval ? "승인됨" : "미승인"}</td>
                <td>
                  <button onClick={() => handleApproval(user.id, true)}>승인</button>
                  <button onClick={() => handleApproval(user.id, false)}>거부</button>
                  <button onClick={() => handleDelete(user.id)}>삭제</button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>

      {/* 교수 목록 */}
      <h2>교수</h2>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>세부 정보</th>
            <th>승인 여부</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {users
            .filter((user) => user.role === "professor")
            .map((user) => (
              <tr key={user.id}>
                <td>{user.userId}</td>
                <td>{renderUserDetails(user)}</td>
                <td>{user.approval ? "승인됨" : "미승인"}</td>
                <td>
                  <button onClick={() => handleApproval(user.id, true)}>승인</button>
                  <button onClick={() => handleApproval(user.id, false)}>거부</button>
                  <button onClick={() => handleDelete(user.id)}>삭제</button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
      <h2>기관 관리자</h2>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>세부 정보</th>
            <th>승인 여부</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {users
            .filter((user) => user.role === "collegeManager")
            .map((user) => (
              <tr key={user.id}>
                <td>{user.userId}</td>
                <td>{renderUserDetails(user)}</td>
                <td>{user.approval ? "승인됨" : "미승인"}</td>
                <td>
                  <button onClick={() => handleApproval(user.id, true)}>승인</button>
                  <button onClick={() => handleApproval(user.id, false)}>거부</button>
                  <button onClick={() => handleDelete(user.id)}>삭제</button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
      <h2>학교 관리자</h2>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>세부 정보</th>
            <th>승인 여부</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {users
            .filter((user) => user.role === "schoolManager")
            .map((user) => (
              <tr key={user.id}>
                <td>{user.userId}</td>
                <td>{renderUserDetails(user)}</td>
                <td>{user.approval ? "승인됨" : "미승인"}</td>
                <td>
                  <button className ="adminButton" onClick={() => handleApproval(user.id, true)}>승인</button>
                  <button className ="adminButton" onClick={() => handleApproval(user.id, false)}>거부</button>
                  <button className ="adminButton" onClick={() => handleDelete(user.id)}>삭제</button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
      </div>
    </div>
  );
};

export default AdminUserManagement;
