import React, { useState, useEffect, useRef } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import axios from 'axios';
import AddEventForm from './AddEventForm';
import EditEventForm from './EditEventForm';
import { jwtDecode } from 'jwt-decode';
import Modal from 'react-modal';  // react-modal 임포트
import Sidebar from '../../Sidebar';

import './Modal.css';
import './Calendar.css';

Modal.setAppElement('#root');

const Calendar = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState('');
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);  // 선택된 일정
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [showAddModal, setShowAddModal] = useState(false);  // 일정 추가 모달 상태
  const [showEditModal, setShowEditModal] = useState(false); // 일정 수정 모달 상태

  const calendarRef = useRef(null); // 캘린더 레퍼런스 추가

  useEffect(() => {
    const handleStorageChange = () => {
      const token = localStorage.getItem("token");
      if (token) {
        try {
          const decodedToken = jwtDecode(token);
          setUserRole(decodedToken.role); // 토큰에서 role 가져오기
          setIsLoggedIn(true);
        } catch (error) {
          console.error("JWT 디코딩 오류:", error);
          setIsLoggedIn(false);
        }
      } else {
        setIsLoggedIn(false);
      }
    };

    handleStorageChange(); // 초기화

    // storage 이벤트 리스너 등록
    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, []);

  useEffect(() => {
    // 전체 일정 로드
    axios.get('http://localhost:8090/api/calendar')
      .then((response) => {
        setEvents(response.data);
        setFilteredEvents(response.data);
      })
      .catch((error) => console.error('Error fetching events:', error));
  }, []);

  const filterEventsByDateRange = (start, end) => {
    const filtered = events.filter(event => {
      const eventStartDate = new Date(event.startDate);
      const eventEndDate = new Date(event.endDate);
      // 날짜 범위 내에 해당하는 일정만 필터링
      return eventStartDate >= start && eventEndDate <= end;
    });
    setFilteredEvents(filtered);
  };

  const handleDateChange = (arg) => {
    const { start, end } = arg;
    filterEventsByDateRange(start, end); // 날짜 범위에 맞는 일정 필터링
  };

  // 이벤트 클릭 시 호출
  const handleEventClick = (info) => {
    const eventData = events.find(event => event.id === info.event.id);
    console.log('클릭한 이벤트:', eventData);  // 이벤트 정보 출력
    if (eventData) {
      setSelectedEvent(eventData);
      setShowEditModal(true);
    }
  };

  // 일정 추가
  const handleEventAdded = (newEvent) => {
    setEvents((prevEvents) => [...prevEvents, newEvent]);
    setShowAddModal(false); // 모달 닫기
  };

  // 일정 수정
  const handleEventUpdated = (updatedEvent) => {
    setEvents((prevEvents) =>
      prevEvents.map((event) =>
        event.id === updatedEvent.id ? updatedEvent : event
      )
    );
    setShowEditModal(false); // 수정 모달 닫기
  };

  // 일정 삭제
  const handleEventDeleted = (eventId) => {
    setEvents((prevEvents) => prevEvents.filter((event) => event.id !== eventId));
    setShowEditModal(false); // 수정 모달 닫기
  };

  // 리스트에서 일정 클릭 시 수정 모달 열기
  const handleListEventClick = (event) => {
    setSelectedEvent(event);
    setShowEditModal(true); // 수정 모달 열기
  };
  
  return (
    <div className="calendar-container">
      <div className="sidebar-container">
        <Sidebar />
      </div>
      {/* 왼쪽 FullCalendar */}
      <div className="calendar-left">
      <FullCalendar
          ref={calendarRef}
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          events={filteredEvents} // 필터링된 일정만 표시
          fixedWeekCount={false}
          eventClick={handleEventClick}
          locale="ko"
          dayCellContent={(arg) => arg.dayNumberText.replace("일", "")}
          datesSet={handleDateChange} // 날짜가 변경될 때마다 필터링
        />
        <div className="button-container">
          {/* 일정 추가 버튼: 로그인 되어 있고 역할이 학생이 아닌 경우만 가능 */}
          {isLoggedIn && userRole !== 'STUDENT' && (
            <button onClick={() => setShowAddModal(true)}>일정 추가</button>
          )}
        </div>
      </div>
      {/* 오른쪽 일정 리스트 */}
      <div className="calendar-right">
        <h2>이번 달의 일정</h2>
        <ul>
          {filteredEvents.map((event) => (
            <li key={event.id} onClick={() => handleListEventClick(event)}>
              <strong>{event.title}</strong> <br />
              {event.startDate} - {event.endDate}
            </li>
          ))}
        </ul>
      </div>

      {/* 일정 추가 모달 */}
      {isLoggedIn && userRole !== 'STUDENT' &&showAddModal && (
        <Modal
          isOpen={showAddModal}
          onRequestClose={() => setShowAddModal(false)}
          className="modal-content"
          overlayClassName="modal-overlay"
        >
          <AddEventForm
            onEventAdded={handleEventAdded}
            onClose={() => setShowAddModal(false)}
          />
        </Modal>
      )}

      {/* 일정 수정 모달 */}
      {isLoggedIn && userRole !== 'STUDENT' &&showEditModal && selectedEvent && (
        <Modal
          isOpen={showEditModal}
          onRequestClose={() => setShowEditModal(false)}
          className="modal-content"
          overlayClassName="modal-overlay"
        >
          <EditEventForm
            selectedEvent={selectedEvent}
            onEventUpdated={handleEventUpdated}
            onEventDeleted={handleEventDeleted}
            onClose={() => setShowEditModal(false)}
          />
        </Modal>
      )}
    </div>
  );
};

export default Calendar;
