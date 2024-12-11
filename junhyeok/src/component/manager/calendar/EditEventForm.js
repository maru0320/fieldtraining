import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import {jwtDecode} from 'jwt-decode';

const EditEventForm = ({ selectedEvent, onEventUpdated, onEventDeleted, onClose }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRoles, setUserRoles] = useState([]);
  const [id,setUserId] = useState([null]);

  useEffect(() => {
    const handleStorageChange = () => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                console.log("Decoded Token:", decodedToken);
                const userId = decodedToken.userId;
                const sub = decodedToken.sub;

                setUserId(id);
                setIsLoggedIn(true);
                setUserRoles(decodedToken.roles || []); // roles 배열 설정

                console.log("User ID:", userId);
                console.log("Long ID:", sub);
            } catch (error) {
                console.error("JWT 디코딩 오류:", error);
                setIsLoggedIn(false);
                setUserRoles([]);
            }
        } else {
            setIsLoggedIn(false);
            setUserRoles([]);
        }
    };

    // 초기 상태 설정
    handleStorageChange();

    // storage 이벤트 리스너 등록
    window.addEventListener("storage", handleStorageChange);

    // 컴포넌트 언마운트 시 리스너 제거
    return () => {
        window.removeEventListener("storage", handleStorageChange);
    };
}, []);
  
  const [formData, setFormData] = useState({
    title: '',
    startDate: '',
    endDate: '',
    description: '',
  });

  // selectedEvent가 변경될 때마다 formData를 갱신
  useEffect(() => {
    if (selectedEvent) {
      setFormData({
        title: selectedEvent.title || '',
        startDate: selectedEvent.startDate || '',
        endDate: selectedEvent.endDate || '',
        description: selectedEvent.extendedProps?.description || '',
      });
    }
  }, [selectedEvent]);

  // 입력 필드 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
  e.preventDefault();

  const token = localStorage.getItem("token");  // 토큰을 가져옵니다.
  
  if (!token) {
    alert('로그인이 필요합니다!');
    return;
  }

  axios.put(
    `http://localhost:8090/api/calendar/${selectedEvent.id}`,
    formData,
    {
      headers: {
        'Authorization': `Bearer ${token}`, // 토큰을 헤더에 포함
      }
    }
  )
    .then((response) => {
      onEventUpdated(response.data); // 수정된 데이터 반영
      alert('일정이 수정되었습니다.');
      onClose(); // 모달 닫기
    })
    .catch((error) => {
      console.error('Error updating event:', error);
      alert('일정을 수정하는 중 오류가 발생했습니다.');
    });
};


  // 일정 삭제 핸들러
  const handleDelete = () => {
    if (window.confirm('정말로 이 일정을 삭제하시겠습니까?')) {
      axios.delete(`http://localhost:8090/api/calendar/${selectedEvent.id}`)
        .then(() => {
          onEventDeleted(selectedEvent.id); // 삭제된 데이터 반영
          alert('일정이 삭제되었습니다.');
          onClose(); // 모달 닫기
        })
        .catch((error) => {
          console.error('Error deleting event:', error);
          alert('일정을 삭제하는 중 오류가 발생했습니다.');
        });
    }
  };

  return (
    <Modal
      isOpen={true}
      onRequestClose={onClose} // 모달 닫기
      contentLabel="일정 수정"
      ariaHideApp={false} // 리액트 모달 사용 시 필수 설정
      className="modal-content"
      overlayClassName="modal-overlay"
    >
      <h2>일정 수정</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>제목</label>
          <input
            type="text"
            name="title"
            value={formData.title}  // formData로 제어된 컴포넌트
            onChange={handleChange}
            placeholder="일정 제목을 입력하세요"
            required
          />
        </div>
        <div>
          <label>시작 날짜</label>
          <input
            type="date"
            name="startDate"
            value={formData.startDate}  // formData로 제어된 컴포넌트
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>종료 날짜</label>
          <input
            type="date"
            name="endDate"
            value={formData.endDate}  // formData로 제어된 컴포넌트
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>설명</label>
          <textarea
            name="description"
            value={formData.description}  // formData로 제어된 컴포넌트
            onChange={handleChange}
            placeholder="일정에 대한 설명을 입력하세요"
          />
        </div>
        <div>
          <button type="submit">수정하기</button>
          <button type="button" className="close-modal" onClick={onClose}>닫기</button>
          <button type="button" onClick={handleDelete} style={{ color: 'red' }}>삭제하기</button>
        </div>
      </form>
    </Modal>
  );
};

export default EditEventForm;
