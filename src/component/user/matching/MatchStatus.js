import React, { useState, useEffect } from "react";
import axios from "axios";

const MatchStatus = () => {
  const studentId = 3; // 기본 학생 아이디
  const [matchStatus, setMatchStatus] = useState(null);

  useEffect(() => {
    // 학생의 매칭 상태 확인 (매칭 중인지 여부)
    axios.get(`http://localhost:8090/matching/status/${studentId}`)
      .then(response => setMatchStatus(response.data.isMatched))
      .catch(error => console.error("Error fetching match status:", error));
  }, [studentId]);

  return (
    <div className="match-status">
      <h1>매칭 상태</h1>
      {matchStatus === null ? (
        <p>로딩 중...</p>
      ) : matchStatus === true ? (
        <p>매칭 중입니다! 선생님과의 매칭이 완료되었습니다.</p>
      ) : (
        <p>매칭이 승인되지 않았습니다.</p>
      )}
    </div>
  );
};

export default MatchStatus;
