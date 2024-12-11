import React, { useState, useEffect } from "react";
import axiosInstance from "../axiosInstance";
import { useParams } from "react-router-dom";
import "./MatchedInfoPage.css";
import Sidebar from "../Sidebar";

const MatchedInfoPage = () => {
  const { matchId } = useParams();
  const [matchedInfo, setMatchedInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMatchedInfo = async () => {
      try {
        const response = await axiosInstance.get(`/admin/matched-info/${matchId}`);
        setMatchedInfo(response.data);
      } catch (err) {
        console.error("Error fetching matched info:", err);
        setError("매칭 상세 정보를 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchMatchedInfo();
  }, [matchId]);

  if (loading) {
    return <p className="loading">매칭 정보를 불러오는 중...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  if (!matchedInfo) {
    return <p className="no-matches">매칭 상세 정보를 찾을 수 없습니다.</p>;
  }

  return (
    
    <div className="matched-info-container">
              <Sidebar />
      <h2>매칭 상세 정보</h2>
      <div className="info-container">
        {/* 학생 정보 */}
        <div className="info-box">
          <h3>학생 정보</h3>
          <p><strong>이름:</strong> {matchedInfo.studentName}</p>
          <p><strong>학교:</strong> {matchedInfo.studentSchool}</p>
          <p><strong>전공:</strong> {matchedInfo.studentSubject}</p>
          <p><strong>이메일:</strong> {matchedInfo.studentEmail}</p>
          <p><strong>대학:</strong> {matchedInfo.studentCollege}</p>
          <p><strong>학과:</strong> {matchedInfo.studentDepartment}</p>
          <p><strong>전화번호:</strong> {matchedInfo.studentPhoneNumber}</p>
        </div>
        
        {/* 선생님 정보 */}
        <div className="info-box">
          <h3>선생님 정보</h3>
          <p><strong>이름:</strong> {matchedInfo.teacherName}</p>
          <p><strong>학교:</strong> {matchedInfo.teacherSchool}</p>
          <p><strong>전공:</strong> {matchedInfo.teacherSubject}</p>
          <p><strong>이메일:</strong> {matchedInfo.teacherEmail}</p>
          <p><strong>전화번호:</strong> {matchedInfo.teacherPhoneNumber}</p>
          <p><strong>사무실 번호:</strong> {matchedInfo.teacherOfficeNumber}</p>
        </div>
      </div>
    </div>
  );
};

export default MatchedInfoPage;
