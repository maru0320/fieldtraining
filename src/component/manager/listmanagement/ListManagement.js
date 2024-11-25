import React, { useState, useEffect } from "react";
import TeachList from "../component/TeachList";
import Header from "../component/Header";
import SearchBar from "../component/SearchBar";
import Pagination from "../component/Pagination";

import "./ListManagement.css";

const ListManagement = () => {
    const [activeList, setActiveList] = useState('');
    const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
    const [searchTerm, setSearchTerm] = useState(''); // 검색어 상태
    const postsPerPage = 10; // 한 페이지에 보여줄 항목 수
    const [teacherData] = useState(
        Array.from({ length: 70 }, (_, index) => ({
            id: index + 1,
            name: `교사 ${index + 1}`,
            subject: `과목 ${index + 1}`,
        }))
    );

    const [studentData] = useState(
        Array.from({ length: 50 }, (_, index) => ({
            id: index + 1,
            name: `실습생 ${index + 1}`,
            course: `과정 ${index + 1}`,
        }))
    );

    // 컴포넌트가 마운트될 때 교사 명단이 자동으로 선택되도록 설정
    useEffect(() => {
        setActiveList('교사');
        setCurrentPage(1); // 첫 페이지로 설정
    }, []);

    // 버튼 클릭 처리
    const handleTeacherListClick = () => {
        setActiveList('교사');
        setCurrentPage(1); // 교사명단 클릭 시 첫 페이지로 리셋
    };

    const handleStudentListClick = () => {
        setActiveList('실습생');
        setCurrentPage(1); // 실습생 명단 클릭 시 첫 페이지로 리셋
    };

    // 검색 처리 함수
    const handleSearch = (searchTerm) => {
        setSearchTerm(searchTerm);
        setCurrentPage(1); // 검색 후 첫 페이지로 리셋
    };

    // 현재 데이터 필터링 (검색어가 있을 경우)
    const currentData = activeList === '교사' ? teacherData : studentData;
    const filteredData = currentData.filter(item => 
        item.name.toLowerCase().includes(searchTerm.toLowerCase()) // 대소문자 구분 없이 필터링
    );

    // 페이지네이션을 위한 데이터 설정
    const totalPages = Math.ceil(filteredData.length / postsPerPage);
    const startIndex = (currentPage - 1) * postsPerPage;
    const currentPosts = filteredData.slice(startIndex, startIndex + postsPerPage);

    return (
        <div className="ListManagement">
            <Header />
            <div className="section">
                <h2 className="title">명단관리</h2>
            </div>
            <div className="section">
                <button onClick={handleTeacherListClick}>교사명단</button>
                <button onClick={handleStudentListClick}>교육 실습생 명단</button>
            </div>
            <div className="section">
                {activeList && <TeachList data={currentPosts} />}
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