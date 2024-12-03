import React, { useState, useEffect } from "react";
import axios from "axios";

import "./MatchCheck.css";

const MatchCheck = () => {
    const [matches, setMatches] = useState([]); // 데이터를 배열로 초기화
    const [error, setError] = useState(null);

    useEffect(() => {
        // 매칭 정보를 한 번에 받아오는 API 호출
        axios
          .get("http://localhost:8090/api/matchingcheck/match")
          .then((response) => {
            setMatches(response.data); // 데이터를 상태에 저장
          })
          .catch((err) => {
            console.error("Error fetching data:", err);
            setError("데이터를 불러오는 중 문제가 발생했습니다.");
          });
      }, []);

    return (
        <div className="MatchCheck">
            <div className="section">
                <h2 className="title">매칭 확인</h2>
            </div>
            {error && (
                <div className="error">
                    <p>{error}</p>
                </div>
            )}
            <div className="section matching">
                {matches.length > 0 ? (
                    matches.map((match, index) => (
                    <div key={index} className="label-container">
                        <label>{match.studentName}</label>
                        <div className="arrow"></div>
                        <label>{match.teacherName}</label>
                        <label>{match.approved ? "승인" : "미승인"}</label>
                    </div>
                ))
                ) : (
                <p>매칭 데이터가 없습니다.</p>
                )}
            </div>
            <div className="section SearchAndPage">
                <div className="SearchBar">
                </div>
                <div className="Pagination">
                </div>
            </div>
        </div>
    );
};

export default MatchCheck;
