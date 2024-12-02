import React, { useEffect, useState } from "react";
import './TeacherPage.css'; // 선생님 페이지 스타일

const TeacherPage = () => {
  const [pendingMatches, setPendingMatches] = useState([]);
  const teacherId = 4; // 선생님 ID

  useEffect(() => {
    // 대기 중인 매칭 목록 가져오기
    fetch(`http://localhost:8090/matching/teacher/${teacherId}/pending`)
      .then((response) => response.json())
      .then((data) => {
        console.log("대기 중인 매칭 목록:", data);
        setPendingMatches(data);
      })
      .catch((error) => console.error("목록 가져오기 실패:", error));
  }, []);

  // 매칭 승인 처리
  const handleApprove = (matchId) => {
    fetch(`http://localhost:8090/matching/approve/${matchId}`, {
      method: "POST",
    })
      .then((response) => {
        if (response.ok) {
          alert("매칭이 승인되었습니다!");
          setPendingMatches((prev) =>
            prev.filter((match) => match.matchId !== matchId)
          ); // 승인된 매칭은 목록에서 제거
        } else {
          alert("매칭 승인 실패");
        }
      })
      .catch((error) => console.error("매칭 승인 오류:", error));
  };

  // 매칭 거절 처리
  const handleReject = (matchId) => {
    fetch(`http://localhost:8090/matching/reject/${matchId}`, {
      method: "POST",
    })
      .then((response) => {
        if (response.ok) {
          alert("매칭이 거절되었습니다!");
          setPendingMatches((prev) =>
            prev.filter((match) => match.matchId !== matchId)
          ); // 거절된 매칭은 목록에서 제거
        } else {
          alert("매칭 거절 실패");
        }
      })
      .catch((error) => console.error("매칭 거절 오류:", error));
  };

  return (
    <div>
      <h1>대기 중인 매칭 목록</h1>
      {pendingMatches.length === 0 ? (
        <p className="no-pending-matches">현재 대기 중인 매칭이 없습니다.</p>
      ) : (
        <div className="pending-matches">
          {pendingMatches.map((match) => (
            <div key={match.matchId} className="match-item">
              <h3>학생 이름: {match.studentName}</h3>
              <p>학생 번호: {match.studentNumber}</p>
              <button
                className="approve-btn"
                onClick={() => handleApprove(match.matchId)}
              >
                승인
              </button>
              <button
                className="reject-btn"
                onClick={() => handleReject(match.matchId)}
              >
                거절
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TeacherPage;
