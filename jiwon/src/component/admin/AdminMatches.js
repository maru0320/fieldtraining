import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // useNavigate로 변경
import axiosInstance from "../axiosInstance";
import "./AdminMatches.css";
import Sidebar from "../Sidebar";

const AdminMatches = () => {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // useNavigate 훅 사용

  useEffect(() => {
    const fetchMatches = async () => {
      try {
        const response = await axiosInstance.get("/admin/matches");
        if (response.status === 204) {
          setMatches([]);
        } else {
          setMatches(response.data);
        }
      } catch (err) {
        console.error("Error fetching matches:", err);
        setError("매칭 정보를 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchMatches();
  }, []);

  const handleMatchClick = (matchId) => {
    navigate(`/admin/matches/${matchId}`); // navigate로 상세 페이지로 이동
  };

  if (loading) {
    return <p className="loading">매칭 정보를 불러오는 중...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  if (matches.length === 0) {
    return <p className="no-matches">현재 등록된 매칭 정보가 없습니다.</p>;
  }

  return (
    <div className="mypage-body">
      <div className="sidebar-container">
        <Sidebar />
      </div>
      <form className="adminMatch-container">
      <h2>매칭 정보</h2>
      <table className="matches-table">
        <thead>
          <tr>
            <th>매칭 ID</th>
            <th>학생 이름</th>
            <th>선생님 이름</th>
            <th>학교 이름</th>
            <th>승인 여부</th>
          </tr>
        </thead>
        <tbody>
          {matches.map((match) => (
            <tr key={match.matchId} onClick={() => handleMatchClick(match.matchId)}>
              <td>{match.matchId}</td>
              <td>{match.studentName}</td>
              <td>{match.teacherName}</td>
              <td>{match.schoolName}</td>
              <td>{match.matchApproved ? "승인됨" : "승인 대기"}</td>
            </tr>
          ))}
        </tbody>
      </table>
      </form>
    </div>
  );
};

export default AdminMatches;
