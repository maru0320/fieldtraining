import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./TeacherPage.css";

const TeacherPage = () => {
  const teacherId = 4; // 선생님 ID
  const [pendingMatches, setPendingMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
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

    fetchPendingMatches();
  }, [teacherId]);

  const approveMatch = async (matchId) => {
    try {
      await axios.post(
        `http://localhost:8090/matching/approve/${matchId}?approve=true`
      );
      alert("매칭이 승인되었습니다.");
      navigate("/MatchStatus");
    } catch (error) {
      console.error("매칭 승인 중 오류가 발생했습니다.", error);
      alert("매칭 승인에 실패했습니다.");
    }
  };

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
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default TeacherPage;
