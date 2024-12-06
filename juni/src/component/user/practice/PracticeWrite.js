import React, { useState, useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";

const StudentAttendance = () => {
  const token = localStorage.getItem("token");
  const decodedToken = jwtDecode(token);
  const studentId = decodedToken.sub; // 학생 ID는 토큰의 "sub" 필드
  const [teacher, setTeacher] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTeacher = async () => {
      try {
        // 학생에 매칭된 선생님 정보를 가져옵니다.
        const response = await axios.get(`http://localhost:8090/attendance/teacher/${studentId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setTeacher(response.data);
      } catch (err) {
        setError("선생님을 찾는 데 실패했습니다.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTeacher();
  }, [studentId, token]);

  const requestAttendance = async () => {
    try {
      const response = await axios.post(
        `http://localhost:8090/attendance/request/${studentId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      alert("출석 요청이 완료되었습니다.");
      navigate("/attendance-status"); // 출석 상태 페이지로 이동
    } catch (err) {
      console.error("출석 요청에 실패했습니다.", err);
      alert("출석 요청에 실패했습니다.");
    }
  };

  if (loading) {
    return <p>로딩 중...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  if (!teacher) {
    return <p>매칭된 선생님이 없습니다.</p>;
  }

  return (
    <div>
      <h2>오늘의 출석 요청</h2>
      <p>오늘 출석 요청을 할 선생님: {teacher.name}</p>
      <p>오늘 날짜: {new Date().toLocaleDateString()}</p>
      <button onClick={requestAttendance}>출석 요청</button>
    </div>
  );
};

export default StudentAttendance;
