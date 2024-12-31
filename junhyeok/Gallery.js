import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import Sidebar from "../../Sidebar";
import axios from "axios";
import "./Gallery.css";

const Gallery = () => {
  const [galleries, setGalleries] = useState([]);
  const [userRole, setUserRole] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);  // 현재 페이지 상태
  const [totalPages, setTotalPages] = useState(0);  // 전체 페이지 수
  const [pageSize] = useState(12);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchInfo = async () => {
      try {
        const token = localStorage.getItem("token");
        if (token) {
          const decodedToken = jwtDecode(token);
          const role = decodedToken.roles;

          let roleName = "unknown";

          if (role.includes("ROLE_SCHOOL_MANAGER")) {
            roleName = "schoolManager";
          } else if (role.includes("ROLE_PROFESSOR")) {
            roleName = "professor";
          } else if (role.includes("ROLE_TEACHER")) {
            roleName = "teacher";
          } else if (role.includes("ROLE_STUDENT")) {
            roleName = "student";
          } else if (role.includes("ROLE_COLLEGE_MANAGER")) {
            roleName = "collegeManager";
          } else if (role.includes("ROLE_ADMIN")) {
            roleName = "admin";
          }

          setUserRole(roleName);
        } else {
          setUserRole("guest");
        }

        const response = await axios.get("http://localhost:8090/api/gallery/galleries/paged", {
          params: {
            page: currentPage,
            size: pageSize,
          },
        });
        setGalleries(response.data.content);
        setTotalPages(response.data.totalPages);
        setError(null);
      } catch (err) {
        console.error("갤러리 데이터 불러오기 오류:", err);
        setError(err.message || "갤러리 데이터를 불러오는 중 오류가 발생했습니다.");
      }
    };
    fetchInfo();
  }, [currentPage]); // 페이지가 변경될 때마다 호출되도록 설정

  const onClickGallerySave = () => {
    navigate("/gallery/galleryWrite");
  };

  // 작성하기 버튼을 보여줄 조건
  const canWriteGallery =
    userRole === "schoolManager" || userRole === "collegeManager" || userRole === "admin";

  // 페이지 버튼 생성
  const renderPageButtons = () => {
    const startPage = Math.floor(currentPage / 5) * 5;
    const endPage = Math.min(startPage + 4, totalPages - 1);
    const pages = [];

    for (let i = startPage; i <= endPage; i++) {
      pages.push(
        <button
          key={i}
          onClick={() => setCurrentPage(i)}
          className={`page-button ${currentPage === i ? "active" : ""}`}
        >
          {i + 1}
        </button>
      );
    }

    return pages;
  };

  return (
    <div className="gallery-wrapper">
      <div className="gallery-container">
        <Sidebar />
        <h1 className="gallery-title">갤러리</h1>
        {error && <p className="error-message">{error}</p>}
        <div className="gallery-section">
          <ul className="gallery-detail">
            {galleries.length > 0 ? (
              galleries.map((gallery) => (
                <li
                  className="detail-container"
                  key={gallery.boardID}
                  onClick={() => navigate(`/gallery/galleryDetail/${gallery.boardID}`)}
                  style={{ cursor: "pointer" }}
                >
                  <span className="img-container">
                    {gallery.files && gallery.files.length > 0 ? (
                      <img
                        className="img"
                        src={`http://localhost:8090${gallery.files[0]?.imgUrl}`}
                        alt="Gallery"
                      />
                    ) : (
                      <p>파일이 없습니다.</p>
                    )}
                  </span>
                  <span className="detail-info">
                    <strong className="detail-title">{gallery.title}</strong>
                    <div className="detail-row">
                      <span className="detail-write">{gallery.writerName}</span>
                      <span className="detail-date">{gallery.date}</span>
                    </div>
                  </span>
                </li>
              ))
            ) : (
              <p>갤러리가 존재하지 않습니다.</p>
            )}
          </ul>
        </div>

        {/* 페이지 전환 버튼 */}
        <div className="pagination">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 0}>
            이전
          </button>
          <div className="page-buttons">{renderPageButtons()}</div>
          <button
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage === totalPages - 1}
          >
            다음
          </button>
        </div>

        {/* 조건에 맞는 경우에만 작성하기 버튼 표시 */}
        {canWriteGallery && (
          <button onClick={onClickGallerySave}>작성하기</button>
        )}
      </div>
    </div>
  );
};

export default Gallery;
