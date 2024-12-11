import React, { useState, useEffect } from "react";
import axios from "axios";

import "./MatchCheck.css";

const MatchCheck = () => {
    const [matches, setMatches] = useState([]); 
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMatches = async () => {
            try {
                const response = await axios.get("http://localhost:8090/api/matchingcheck/match");
                console.log("Response Data: ", response.data);
                
                if (response.data) {
                    setMatches(response.data);
                } else {
                    setMatches([]); // 빈 리스트 처리
                }
            } catch (err) {
                console.error("Error fetching match data", err);
                setError("매칭 정보를 불러오는 중 문제가 발생했습니다.");
            }
        };

        fetchMatches();
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
                            <label>학생 이름: {match.studentName}</label>
                            <div className="arrow"></div>
                            <label>교사 이름: {match.teacherName}</label>
                            <label>매칭 상태: {match.matchStatus}</label>
                            <label>승인 여부: {match.matchApproved ? "승인" : "미승인"}</label>
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
