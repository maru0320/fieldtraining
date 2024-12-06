import React, { useState, useEffect } from "react";
import Header from "../component/Header";
import SearchBar from "../component/SearchBar";
import Pagination from "../component/Pagination";

import "./MatchCheck.css";

const MatchCheck = () => {
    // 가상 데이터 (나중에 DB 연동 시 API 호출로 대체)
    const [students, setStudents] = useState("교육실습생");
    const [teachers, setTeachers] = useState("교사");
    const [matchingStatus, setMatchingStatus] = useState("매칭 중...");

    // 예시로 useEffect를 사용하여 데이터를 가져오는 부분을 작성할 수 있습니다.
    // useEffect(() => {
    //   fetch('/api/your-data-endpoint')  // 실제 DB 연동 API로 대체
    //     .then(response => response.json())
    //     .then(data => {
    //       setStudents(data.students);
    //       setTeachers(data.teachers);
    //       setMatchingStatus(data.status);
    //     });
    // }, []);

    return (
        <div className="MatchCheck">
            <Header />
            <div className="section">
                <h2 className="title">매칭 확인</h2>
            </div>
            <div className="section matching">
                <div className="label-container">
                    <label>{students}</label>
                    <div className="arrow"></div>
                    <label>{teachers}</label>
                    <label>{matchingStatus}</label>
                </div>
            </div>
            <div className="section SearchAndPage">
                <div className="SearchBar">
                    <SearchBar />
                </div>
                <div className="Pagination">
                    <Pagination />
                </div>
            </div>
        </div>
    );
};

export default MatchCheck;