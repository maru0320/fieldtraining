import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import './StudentPage.css';
import { jwtDecode } from 'jwt-decode';
import axiosInstance from "../../axiosInstance";
import Sidebar from "../../Sidebar";

const StudentPage = () => {
  const token = localStorage.getItem("token");
  const [teachers, setTeachers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);


  const decodedToken = jwtDecode(token);
        console.log("Decoded Token:", decodedToken);
        const studentId = decodedToken.userId;

  const navigate = useNavigate(); // navigate 훅 사용

  useEffect(() => {
    const fetchTeachers = async () => {
      try {
        const response = await axiosInstance.get(`/matching/teachers/${studentId}`);
        setTeachers(response.data);
      } catch (err) {
        console.error("Error fetching teachers:", err);
        setError("선생님 목록을 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchTeachers();
  }, []);

  if (loading) {
    return <p className="loading">상태를 불러오는 중...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  if (teachers.length === 0) {
    return <p className="no-teachers">현재 매칭 가능한 선생님이 없습니다.</p>;
  }

  const applyMatch = async (teacherId) => {
    try {
      const response = await axiosInstance.post("/matching/apply", {
        studentId,  // 학생 ID
        teacherId   // 선택한 선생님 ID
      });

      if (response.status === 200) {
        alert("매칭 신청이 완료되었습니다.");
        navigate("/matchstatus");  // 신청 후 매칭 상태 페이지로 이동
      }
    } catch (err) {
      console.error("Error applying for match:", err);
      alert("매칭 신청에 실패했습니다.");
    }
  };

  return (
    <div className="mypage-body">
    <div className="sidebar-container">
      <Sidebar />
    </div>
    <form className="studentmatching-container">
      <h2>매칭 가능한 선생님 목록</h2>
      <div className="teacher-list">
        {teachers.map((teacher) => (
          <div className="teacher-item" key={teacher.id}>
            <p>{teacher.name}</p>
            <button onClick={() => applyMatch(teacher.id)}>매칭 신청</button>
          </div>
        ))}
      </div>
    </form>
    </div>
  );
};

export default StudentPage;
