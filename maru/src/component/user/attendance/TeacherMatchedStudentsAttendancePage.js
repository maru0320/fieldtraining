import React, { useState, useEffect } from "react";
import axios from "axios";
import {jwtDecode} from "jwt-decode";
import "./TeacherMatchedStudentsAttendancePage.css";
import axiosInstance from "../../axiosInstance";

const TeacherMatchedStudentsAttendancePage = () => {
  const token = localStorage.getItem("token");
  const [attendanceRecords, setAttendanceRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // JWT에서 teacherId 추출
  const getTeacherIdFromToken = (token) => {
    try {
      if (!token) {
        console.error("토큰이 없습니다.");
        return null;
      }
      const decodedToken = jwtDecode(token);
      console.log("디코드된 토큰:", decodedToken); // 디버깅용
      return decodedToken.userId || null; // teacherId는 JWT의 sub 필드
    } catch (err) {
      console.error("JWT 디코딩 오류:", err);
      return null;
    }
  };

  const teacherId = getTeacherIdFromToken(token);

  useEffect(() => {
    const fetchAttendanceRecords = async () => {
      if (!teacherId) {
        setError("teacherId를 추출할 수 없습니다. 유효한 토큰을 확인하세요.");
        setLoading(false);
        return;
      }

      try {
        const response = await axiosInstance.get(
          `/attendance/status/teacher/${teacherId}`
        );
        console.log("API 응답 데이터:", response.data); // API 데이터 확인
        setAttendanceRecords(response.data);
      } catch (err) {
        console.error("출석 기록을 불러오는 데 실패했습니다:", err);
        setError("출석 기록을 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchAttendanceRecords();
  }, [teacherId]);

  if (loading) {
    return <p>출석 기록을 불러오는 중...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div>
      <h2>매칭된 학생의 출석 기록</h2>
      {attendanceRecords.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>날짜</th>
              <th>학생 이름</th>
              <th>출석 상태</th>
            </tr>
          </thead>
          <tbody>
            {attendanceRecords.map((record) => (
              <tr key={record.attendanceId}>
                <td>{record.date}</td>
                <td>{record.studentName || "정보 없음"}</td> {/* studentName 직접 사용 */}
                <td>{record.status || "정보 없음"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>출석 기록이 없습니다.</p>
      )}
    </div>
  );
};

export default TeacherMatchedStudentsAttendancePage;
