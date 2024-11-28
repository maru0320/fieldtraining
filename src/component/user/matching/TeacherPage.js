import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TeacherPage.css";

const TeacherPage = () => {
  const teacherId = 4; // 선생님 ID
  const [pendingMatches, setPendingMatches] = useState([]);
  const [loading, setLoading] = useState(true);

  // 승인 대기 목록 가져오기
  const fetchPendingMatches = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8090/matching/pending/${teacherId}`
      );
      setPendingMatches(response.data);
    } catch (error) {
      console.error("승인 대기 목록을 불러오는 데 실패했습니다.", error);
    } finally {
      setLoading(false);
    }
  };

  // 매칭 승인
  const approveMatch = async (matchId) => {
    try {
      await axios.post(
        `http://localhost:8090/matching/approve/${matchId}?approve=true`
      );
      alert("매칭이 승인되었습니다.");
      fetchPendingMatches(); // 목록 갱신
    } catch (error) {
      console.error("매칭 승인 중 오류가 발생했습니다.", error);
      alert("매칭 승인에 실패했습니다.");
    }
  };

  // 매칭 거절
  const rejectMatch = async (matchId) => {
    try {
      await axios.post(
        `http://localhost:8090/matching/approve/${matchId}?approve=false`
      );
      alert("매칭이 거절되었습니다.");
      fetchPendingMatches(); // 목록 갱신
    } catch (error) {
      console.error("매칭 거절 중 오류가 발생했습니다.", error);
      alert("매칭 거절에 실패했습니다.");
    }
  };

  useEffect(() => {
    fetchPendingMatches();
  }, []);

  return (
    <div className="teacher-page">
      <h1>승인 대기 매칭</h1>
      {loading ? (
        <p>로딩 중...</p>
      ) : pendingMatches.length === 0 ? (
        <p>현재 승인 대기 중인 매칭이 없습니다.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>학생 이름</th>
              <th>학생 번호</th>
              <th>승인</th>
              <th>거절</th>
            </tr>
          </thead>
          <tbody>
            {pendingMatches.map((match) => (
              <tr key={match.matchId}>
                <td>{match.studentName}</td>
                <td>{match.studentNumber}</td>
                <td>
                  <button
                    className="approve-button"
                    onClick={() => approveMatch(match.matchId)}
                  >
                    승인
                  </button>
                </td>
                <td>
                  <button
                    className="reject-button"
                    onClick={() => rejectMatch(match.matchId)}
                  >
                    거절
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default TeacherPage;
