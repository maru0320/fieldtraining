import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./TeacherAttendanceApproval.css"
import axiosInstance from "../../axiosInstance";
import Sidebar from "../../Sidebar";

const TeacherAttendanceApproval = () => {
  const token = localStorage.getItem("token");
  const [attendanceRequests, setAttendanceRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // JWT 토큰 디코딩하여 teacherId 가져오기
  const decodedToken = jwtDecode(token);
  const teacherId = decodedToken.userId;
  console.log(decodedToken);  // 디코딩된 JWT 확인
  console.log("Teacher ID:", teacherId);  // teacherId 확인

  const navigate = useNavigate();

  useEffect(() => {
    const fetchAttendanceRequests = async () => {
      try {
        const response = await axiosInstance.get(
          `/attendance/requests/${teacherId}`
        );
        console.log("Fetched data:", response);  // API 응답 데이터 전체 확인

        const pendingRequests = response.data.filter((request) => {
          console.log(request.status);  // 각 요청의 status 확인
          return request.status === "PENDING";
        });

        setAttendanceRequests(pendingRequests);
      } catch (err) {
        console.error("출석 요청을 불러오는 데 실패했습니다:", err);
        setError("출석 요청을 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchAttendanceRequests();
  }, [teacherId]);

  const updateAttendanceStatus = async (attendanceId, status) => {
    try {
      // URL 쿼리 파라미터로 상태 전달
      const response = await axiosInstance.put(
        `/attendance/update/${attendanceId}?attendanceStatus=${status}`,
        null,  // 본문 내용은 필요하지 않음
      );

      alert("출석 상태가 성공적으로 업데이트되었습니다.");
      setAttendanceRequests((prevRequests) =>
        prevRequests.filter((request) => request.attendanceId !== attendanceId)
      );
      navigate("/TeacherAttendanceApproval");
    } catch (err) {
      console.error("출석 상태 업데이트에 실패했습니다:", err);
      alert("출석 상태 업데이트에 실패했습니다.");
    }
  };

  if (loading) {
    return <p>출석 요청을 불러오는 중...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div className="mypage-body">
    <div className="sidebar-container">
      <Sidebar />
    </div>
    <form className="teacherAttendance-container">
      <h2>출석 요청 승인</h2>
      <table>
        <thead>
          <tr>
            <th>학생 이름</th>
            <th>날짜</th>
            <th>출석 상태</th>
          </tr>
        </thead>
        <tbody>
          {attendanceRequests.length > 0 ? (
            attendanceRequests.map((request) => (
              <tr key={request.attendanceId}>
                <td>{request.studentName}</td>
                <td>{request.date}</td>
                <td>
                  <button
                    onClick={() => updateAttendanceStatus(request.attendanceId, "ATTENDED")}
                  >
                    출석
                  </button>
                  <button
                    onClick={() => updateAttendanceStatus(request.attendanceId, "LATE")}
                  >
                    지각
                  </button>
                  <button
                    onClick={() => updateAttendanceStatus(request.attendanceId, "EARLY_LEAVE")}
                  >
                    조퇴
                  </button>
                  <button
                    onClick={() => updateAttendanceStatus(request.attendanceId, "ABSENT")}
                  >
                    결석
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3">현재 승인 대기 중인 출석 요청이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>
    </form>
    </div>
  );
};

export default TeacherAttendanceApproval;
