import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./AttendanceStatusPage.css"


const AttendanceStatusPage = () => {
  const token = localStorage.getItem("token");
  const [attendanceList, setAttendanceList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // JWT 토큰 디코딩하여 studentId 가져오기
  const decodedToken = jwtDecode(token);
  const studentId = decodedToken.sub;
  console.log(decodedToken);  // 디코딩된 JWT 확인
  console.log("Student ID:", studentId);  // studentId 확인

  const navigate = useNavigate();

  useEffect(() => {
    const fetchAttendanceStatus = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8090/attendance/status/${studentId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("Fetched data:", response);  // API 응답 데이터 전체 확인

        setAttendanceList(response.data);  // 출석 목록 상태 설정
      } catch (err) {
        console.error("출석 목록을 불러오는 데 실패했습니다:", err);
        setError("출석 목록을 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchAttendanceStatus();
  }, [studentId]);

  if (loading) {
    return <p>출석 목록을 불러오는 중...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div>
      <h2>출석 상태</h2>
      <table>
        <thead>
          <tr>
            <th>날짜</th>
            <th>출석 상태</th>
          </tr>
        </thead>
        <tbody>
          {attendanceList.length > 0 ? (
            attendanceList.map((attendance) => (
              <tr key={attendance.attendanceId}>
                <td>{attendance.date}</td>
                <td>{attendance.status}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="2">출석 목록이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default AttendanceStatusPage;