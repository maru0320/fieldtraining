import React, { useState } from "react";
import SearchBar from "../../SearchBar";
import Pagination from "../../Pagination";
import "../../Board.css";
import { useNavigate } from "react-router-dom";

const FormBoard = ({posts}) => {
    const navigate = useNavigate();
    // 글쓰기 버튼 클릭 시 /write 경로로 이동
    const handleWriteClick = () => {
        navigate("/FormWrite");
    };

    // 검색어 상태
    const [searchTerm, setSearchTerm] = useState("");

    // 검색된 게시물만 필터링
    const filteredPosts = (posts || []).filter(post => 
        post.title.toLowerCase().includes(searchTerm.toLowerCase()) // 대소문자 구분 없이 필터링
    );
    // 검색 처리 함수
    const handleSearch = (searchTerm) => {
        setSearchTerm(searchTerm); // 검색어 상태 설정
        setCurrentPage(1); // 검색 후 첫 페이지로 리셋
    };

    //페이지네이션

    // 페이지 관련 상태 설정
    const [currentPage, setCurrentPage] = useState(1);
    const postsPerPage = 10;

    // 총 페이지 수 계산
    const totalPages = Math.ceil(filteredPosts.length / postsPerPage);
    const startIndex = (currentPage - 1) * postsPerPage;
    const currentPosts = filteredPosts.slice(startIndex, startIndex + postsPerPage);

    return (
        <div className="Board">
            <div className="section">
                <h2 className="title">양식 게시판</h2>
            </div>
            <div className="section board-content">
                <table className="board-table">
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>제목</th>
                            <th>작성일</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentPosts.length > 0 ? (
                            currentPosts.map((post) => (
                                <tr key={post.id}>
                                    <td>{post.id}</td>
                                    <td>{post.title}</td>
                                    <td>{post.date}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="3">검색 결과가 없습니다.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
            <div className="section SearchAndPage">
                <div className="SearchBar">
                    <SearchBar onSearch={handleSearch} />
                </div>
                <Pagination 
                    currentPage={currentPage} 
                    setCurrentPage={setCurrentPage} 
                    totalPages={totalPages} 
                />
                <button onClick={handleWriteClick}>글쓰기</button>
            </div>
        </div>
    );
};

export default FormBoard;