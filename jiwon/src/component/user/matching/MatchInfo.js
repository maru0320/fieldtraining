import React, { useState, useEffect } from "react";
import {jwtDecode} from "jwt-decode"; // import 수정
import axiosInstance from "../../axiosInstance";
import "./MatchInfo.css";
import Sidebar from "../../Sidebar";

const MatchInfo = () => {
  const token = localStorage.getItem("token");
  const [matchInfo, setMatchInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // JWT 디코딩
  const decodedToken = jwtDecode(token);
  const userId = decodedToken.userId;
  const userRole = decodedToken.roles;

  console.log("User Role:", userRole);

  // 매칭 정보 가져오기
  useEffect(() => {
    const fetchMatchInfo = async () => {
      try {
        // 1. 매칭 ID 가져오기
        const matchIdResponse = await axiosInstance.get(`/matching/match-id/${userId}`);
        const matchId = matchIdResponse.data.matchId;

        if (!matchId) {
          throw new Error("매칭 ID가 존재하지 않습니다.");
        }

        // 2. 매칭된 정보 가져오기
        const matchInfoResponse = await axiosInstance.get(`/matching/matched-info/${matchId}`);
        setMatchInfo(matchInfoResponse.data);
        console.log("Fetched match info:", matchInfoResponse.data);
      } catch (err) {
        console.error("Error fetching match info:", err);
        setError("매칭 정보를 가져오는 중 문제가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchMatchInfo();
  }, [userId]);

  // 매칭 삭제
  const handleDeleteMatch = async (matchId) => {
    console.log("Deleting match with ID:", matchId);
    if (!matchId) {
      alert("삭제할 매칭 ID가 없습니다.");
      return;
    }

    try {
      const response = await axiosInstance.delete(`/matching/delete/${matchId}`);
      if (response.status === 200) {
        alert("매칭이 성공적으로 삭제되었습니다.");
        setMatchInfo(null);
      } else {
        console.error("삭제 요청 실패:", response);
        setError("매칭 삭제 중 문제가 발생했습니다.");
      }
    } catch (error) {
      console.error("매칭 삭제 중 오류 발생:", error);
      setError("매칭 삭제 중 오류가 발생했습니다.");
    }
  };

  // 로딩 상태
  if (loading) {
    return <p className="loading">매칭 정보를 불러오는 중...</p>;
  }

  // 오류 상태
  if (error) {
    return <p className="error">{error}</p>;
  }

  // 매칭 정보가 없을 때
  if (!matchInfo) {
    return <p className="no-match">현재 매칭된 정보가 없습니다.</p>;
  }

  // 매칭 정보 렌더링
  return (
    <div className="mypage-body">
      <div className="sidebar-container">
        <Sidebar />
      </div>
    <form className="match-info-container">
      <h2>매칭된 정보</h2>
      <div className="info-section">
        <h3>학생 정보</h3>
        <p>이름: {matchInfo.studentName}</p>
        <p>실습 학교: {matchInfo.studentSchool}</p>
        <p>과목: {matchInfo.studentSubject}</p>
        <p>이메일: {matchInfo.studentEmail}</p>
        <p>학교: {matchInfo.studentCollege}</p>
        <p>학과: {matchInfo.studentDepartment}</p>
        <p>휴대폰 번호: {matchInfo.studentPhoneNumber}</p>
      </div>
      <div className="info-section">
        <h3>선생님 정보</h3>
        <p>이름: {matchInfo.teacherName}</p>
        <p>실습 학교: {matchInfo.teacherSchool}</p>
        <p>과목: {matchInfo.teacherSubject}</p>
        <p>이메일: {matchInfo.teacherEmail}</p>
        <p>휴대폰 번호: {matchInfo.teacherPhoneNumber}</p>
        <p>사무실 번호: {matchInfo.teacherOfficeNumber}</p>
      </div>

      {userRole.includes("ROLE_TEACHER") && (
        <button
          onClick={() => handleDeleteMatch(matchInfo.id)} // matchInfo.id 전달
          className="delete-btn"
        >
          매칭 삭제
        </button>
      )}
    </form>
    </div>
  );
};

export default MatchInfo;
