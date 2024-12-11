import React, { useState } from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
import Modal from 'react-modal';

const AddEventForm = ({ onEventAdded, onClose }) => {
    const [title, setTitle] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token');
        let userRole = null;

        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                userRole = decodedToken.role; // 토큰에서 역할 추출
            } catch (error) {
                console.error("JWT 디코딩 오류:", error);
                alert('유효하지 않은 토큰입니다. 다시 로그인하세요.');
                return;
            }
        } else {
            alert('로그인이 필요합니다.');
            return;
        }

        const newEvent = {
            title,
            startDate,
            endDate,
            description,
            role: userRole, // 사용자 역할을 포함하여 전송
        };

        axios.post('http://localhost:8090/api/calendar', newEvent, {
            headers: {
                Authorization: `Bearer ${token}`, // 인증 헤더 추가
            },
        })
            .then(response => {
                onEventAdded(response.data); // 새로운 일정 추가
                alert('일정이 추가되었습니다.');
                onClose(); // 모달 닫기
            })
            .catch(error => {
                console.error("에러: ", error);
                alert('일정을 추가하는 중 오류가 발생했습니다.');
            });
    };

    return (
        <Modal
            isOpen={true}
            onRequestClose={onClose} // 모달 닫기
            contentLabel="일정 추가"
            ariaHideApp={false} // 리액트 모달 사용 시 필수 설정
            className="modal-content"
            overlayClassName="modal-overlay"
            >
            <h2>일정 추가</h2>
            <form onSubmit={handleSubmit}>
                <div>
                <label>제목</label>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="일정 제목을 입력하세요"
                    required
                />
                </div>
                <div>
                <label>시작 날짜</label>
                <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    required
                />
                </div>
                <div>
                <label>종료 날짜</label>
                <input
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    required
                />
                </div>
                <div>
                <label>설명</label>
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="일정에 대한 설명을 입력하세요"
                />
                </div>
                <button type="submit">일정 추가</button>
            </form>
            <button className="close-modal" onClick={onClose}>닫기</button>
        </Modal>
    );
};

export default AddEventForm;