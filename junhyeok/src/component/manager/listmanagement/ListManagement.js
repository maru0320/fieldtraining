import React, { useState, useEffect } from "react";
import axios from "axios";
import SearchBar from "../../SearchBar";
import Pagination from "../../Pagination";

import "./ListManagement.css";

const ListManagement = () => {
    const [users, setUsers] = useState([]); // 전체 사용자 데이터
    const [activeList, setActiveList] = useState('교사');
    const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
    const [searchTerm, setSearchTerm] = useState(''); // 검색어 상태
    const postsPerPage = 10; // 한 페이지에 보여줄 항목 수

    // 버튼 클릭 처리
    const handleTeacherListClick = () => {
        setActiveList('교사');
        setCurrentPage(1); // 교사명단 클릭 시 첫 페이지로 리셋
    };

    const handleStudentListClick = () => {
        setActiveList('실습생');
        setCurrentPage(1); // 실습생 명단 클릭 시 첫 페이지로 리셋
    };

    // API 호출로 사용자 데이터 가져오기
    useEffect(() => {
        // 활성화된 리스트에 따라 교사 또는 실습생 데이터 요청
        const url =
            activeList === '교사'
                ? "http://localhost:8090/api/listmanagement/teacher"
                : activeList === '실습생'
                ? "http://localhost:8090/api/listmanagement/student"
                : "http://localhost:8090/api/listmanagement"; // 기본적으로 전체 리스트 호출

        axios.get(url)
            .then((response) => {
                console.log("응답 데이터:", response.data); // 응답 확인
                if (Array.isArray(response.data)) {
                    setUsers(response.data);
                } else if (response.data && Array.isArray(response.data.data)) {
                    setUsers(response.data.data); // data 안에 배열이 있는 경우
                } else {
                    console.error("예상치 못한 응답 데이터 형식입니다.");
                }
            })
            .catch((error) => {
                console.error("데이터 로딩 실패:", error);
            });
    }, [activeList]); // activeList가 변경될 때마다 호출

    // 검색 처리 함수
    const handleSearch = (searchTerm) => {
        setSearchTerm(searchTerm);
        setCurrentPage(1); // 검색 후 첫 페이지로 리셋
    };

    const filteredData = users.filter((user) =>
        user.name.toLowerCase().includes(searchTerm.toLowerCase()) // 대소문자 구분 없이 필터링
    );

    // 페이지네이션을 위한 데이터 설정
    const totalPages = Math.ceil(filteredData.length / postsPerPage);
    const startIndex = (currentPage - 1) * postsPerPage;
    const currentPosts = filteredData.slice(startIndex, startIndex + postsPerPage);

    return (
        <div className="ListManagement">
            <div className="section">
                <h2 className="title">명단관리</h2>
            </div>
            <div className="section">
                <button onClick={handleTeacherListClick}>교사명단</button>
                <button onClick={handleStudentListClick}>교육 실습생 명단</button>
            </div>
            <div className="section"> {/*학생과 교사 데이터가 나열되는곳*/}
                <ul>
                    {currentPosts.map((user) => (
                        <li key={user.id}>{user.name}</li>
                    ))}
                </ul>
            </div>
            <div className="section SearchAndPage">
                <div className="SearchBar">
                    <SearchBar onSearch={handleSearch} />
                </div>
                <div className="Pagination">
                    <Pagination
                        currentPage={currentPage}
                        setCurrentPage={setCurrentPage}
                        totalPages={totalPages}
                    />
                </div>
            </div>
        </div>
    );
};

export default ListManagement;