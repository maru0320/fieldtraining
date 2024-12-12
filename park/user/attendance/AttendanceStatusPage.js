import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./AttendanceStatusPage.css";
import axiosInstance from "../../axiosInstance";
import Sidebar from "../../Sidebar";

const AttendanceStatusPage = () => {
  const token = localStorage.getItem("token");
  const [attendanceList, setAttendanceList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // JWT 토큰 디코딩하여 studentId 가져오기
  const decodedToken = jwtDecode(token);
  const studentId = decodedToken.userId;

  const navigate = useNavigate();

  const translateAttendanceStatus = (status) => {
    switch (status) {
      case "ATTENDED":
        return "출석";
      case "ABSENT":
        return "결석";
      case "LATE":
        return "지각";
      case "EARLY_LEAVE":
        return "조퇴";
      default:
        return "승인 대기";
    }
  };

  useEffect(() => {
    const fetchAttendanceStatus = async () => {
      try {
        const response = await axiosInstance.get(
          `/attendance/status/${studentId}`
        );
        setAttendanceList(response.data);
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
    <div className="mypage-body">
      <div className="sidebar-container">
        <Sidebar />
      </div>
      <form className="studentAttendance-contariner">
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
                  <td>{translateAttendanceStatus(attendance.status)}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="2">출석 목록이 없습니다.</td>
              </tr>
            )}
          </tbody>
        </table>
      </form>
    </div>
  );
};

export default AttendanceStatusPage;
