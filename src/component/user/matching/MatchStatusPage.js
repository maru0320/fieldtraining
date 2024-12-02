import React, { useState, useEffect } from "react";
import axios from "axios";
import './MatchStatusPage.css'; // 스타일 import

const MatchStatusPage = () => {
  const [matchStatus, setMatchStatus] = useState(null);
  const [loading, setLoading] = useState(true);
  const studentId = 3;  // 학생 ID 고정

  useEffect(() => {
    const fetchMatchStatus = async () => {
      try {
        const response = await axios.get(`http://localhost:8090/matching/status/${studentId}`);
        setMatchStatus(response.data);
      } catch (err) {
        console.error("Error fetching match status:", err);
        setMatchStatus("매칭 상태를 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchMatchStatus();
  }, [studentId]);

  return (
    <div className="match-status-card">
      <h2>매칭 상태</h2>
      {loading ? (
        <p className="loading">매칭 상태를 불러오는 중...</p>
      ) : matchStatus === "매칭 대기중" ? (
        <p className="pending">매칭 대기 중입니다.</p>
      ) : matchStatus === "매칭 승인됨" ? (
        <p className="approved">매칭이 승인되었습니다!</p>
      ) : matchStatus === "매칭 거절됨" ? (
        <p className="rejected">매칭이 거절되었습니다.</p>
      ) : (
        <p className="error">{matchStatus}</p>
      )}
    </div>
  );
};

export default MatchStatusPage;