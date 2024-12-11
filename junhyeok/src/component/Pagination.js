import React from 'react';
import "./Pagination.css";

const Pagination = ({ currentPage, setCurrentPage, totalPages }) => {
    // 페이지 번호 제한
    const pageLimit = 7;

    // 페이지 번호 범위 계산
    const startPage = Math.max(1, currentPage - Math.floor(pageLimit / 2));
    const endPage = Math.min(totalPages, startPage + pageLimit - 1);

    // 페이지가 7개 이하로 나타날 수 있도록 범위 보장
    const pageRange = [];
    for (let i = startPage; i <= endPage; i++) {
        pageRange.push(i);
    }

    // 이전 페이지 버튼 클릭 시
    const handlePrevClick = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    // 다음 페이지 버튼 클릭 시
    const handleNextClick = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    return (
        <div className="pagination-container">
            {/* 이전 페이지 버튼 */}
            <button onClick={handlePrevClick} disabled={currentPage === 1}>
                이전
            </button>

            {/* 페이지 번호 버튼 */}
            {pageRange.map(page => (
                <button
                    key={page}
                    onClick={() => setCurrentPage(page)}
                    className={`page-button ${currentPage === page ? 'active' : ''}`}
                >
                    {page}
                </button>
            ))}

            {/* 다음 페이지 버튼 */}
            <button onClick={handleNextClick} disabled={currentPage === totalPages}>
                다음
            </button>
        </div>
    );
};

export default Pagination;