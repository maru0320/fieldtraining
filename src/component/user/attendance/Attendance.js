import React, { useState } from "react";

const Attendance = () => {
  // 날짜 리스트
  const dates = [
    '2024-11-01', '2024-11-02', '2024-11-03', 
    '2024-11-04', '2024-11-05', '2024-11-06', '2024-11-07'
  ];

  // 초기 상태는 모두 미승인으로 설정
  const [attendance, setAttendance] = useState(
    dates.reduce((acc, date) => ({ ...acc, [date]: '미승인' }), {})
  );

  // 모든 날짜의 출석 상태를 승인/미승인으로 변경
  const handleToggleApproval = () => {
    const allApproved = Object.values(attendance).every(status => status === '승인');
    const newStatus = allApproved ? '미승인' : '승인';

    const updatedAttendance = dates.reduce((acc, date) => ({
      ...acc,
      [date]: newStatus
    }), {});

    setAttendance(updatedAttendance);
  };

  return (
    <div className="container">
      <h1>날짜별 출석 현황</h1>
      <table>
        <thead>
          <tr>
            <th>날짜</th>
            <th>출석 여부</th>
          </tr>
        </thead>
        <tbody>
          {dates.map((date) => (
            <tr key={date}>
              <td>{date}</td>
              <td>{attendance[date]}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button onClick={handleToggleApproval}>출석</button>
    </div>
  );
};

export default Attendance;
