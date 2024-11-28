// StudentPage.js

import React, { useState, useEffect } from "react";
import axios from "axios";
import './StudentPage.css';

const StudentPage = () => {
  const [teachers, setTeachers] = useState([]);
  const studentId = 3; // 기본 학생 아이디

  useEffect(() => {
    // 선생님 목록 가져오기
    axios.get(`http://localhost:8090/matching/teachers?studentId=${studentId}`)
      .then(response => setTeachers(response.data))
      .catch(error => console.error("Error fetching teachers:", error));
  }, [studentId]);

  const handleSubmit = (event) => {
    event.preventDefault();
    const teacherId = event.target.teacher.value;

    axios.post('http://localhost:8090/matching/apply', {
      studentId,
      teacherId,
    })
      .then(response => {
        alert(response.data);
      })
      .catch(error => {
        console.error("Error applying match:", error);
      });
  };

  return (
    <div className="student-container">
      <form className="student-form" onSubmit={handleSubmit}>
        <h2>선생님 신청하기</h2>
        <label htmlFor="teacher">선생님 선택:</label>
        <select id="teacher" name="teacher" required>
          {teachers.map(teacher => (
            <option key={teacher.id} value={teacher.id}>{teacher.name}</option>
          ))}
        </select>
        <button type="submit">신청하기</button>
      </form>
    </div>
  );
};

export default StudentPage;
