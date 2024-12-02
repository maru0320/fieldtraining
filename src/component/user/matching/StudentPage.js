import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./StudentPage.css";

const StudentPage = () => {
  const [teachers, setTeachers] = useState([]);
  const [matchStatus, setMatchStatus] = useState(null);
  const studentId = 3; // 기본 학생 ID
  const navigate = useNavigate();

  useEffect(() => {
    // 매칭 상태 확인
    axios
      .get(`http://localhost:8090/matching/status/${studentId}`)
      .then((response) => {
        setMatchStatus(response.data.isMatched);
      })
      .catch((error) => console.error("Error fetching match status:", error));
  }, [studentId]);

  useEffect(() => {
    if (matchStatus === true) {
      navigate("/MatchStatus");
    }
  }, [matchStatus, navigate]);

  useEffect(() => {
    // 선생님 목록 가져오기
    axios
      .get(`http://localhost:8090/matching/teachers?studentId=${studentId}`)
      .then((response) => setTeachers(response.data))
      .catch((error) => console.error("Error fetching teachers:", error));
  }, [studentId]);

  const handleSubmit = (event) => {
    event.preventDefault();
    const teacherId = event.target.teacher.value;

    axios
      .post("http://localhost:8090/matching/apply", {
        studentId,
        teacherId,
      })
      .then(() => {
        alert("신청이 완료되었습니다.");
        setMatchStatus(false);
      })
      .catch((error) => {
        console.error("Error applying match:", error);
      });
  };

  if (matchStatus === null) return <p>로딩 중...</p>;

  return matchStatus === false ? (
    <div className="student-container">
      <h2>매칭 신청 중...</h2>
    </div>
  ) : (
    <div className="student-container">
      <form className="student-form" onSubmit={handleSubmit}>
        <h2>선생님 신청하기</h2>
        <label htmlFor="teacher">선생님 선택:</label>
        <select id="teacher" name="teacher" required>
          {teachers.map((teacher) => (
            <option key={teacher.id} value={teacher.id}>
              {teacher.name}
            </option>
          ))}
        </select>
        <button type="submit">신청하기</button>
      </form>
    </div>
  );
};

export default StudentPage;
