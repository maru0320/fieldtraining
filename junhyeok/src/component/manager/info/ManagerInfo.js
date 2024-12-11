import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

const BASE_URL = "http://localhost:8090/api/managerInfo";

const ManagerInfo = () => {
  const [collegeManagerInfo, setCollegeManagerInfo] = useState(null);
  const [schoolManagerInfo, setSchoolManagerInfo] = useState(null);
  const [loadingStates, setLoadingStates] = useState({ college: true, school: true });
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        const userId = decodedToken.userId;
        const role = decodedToken.role;

        fetchManagerInfoByRole(role, userId);
      } catch (error) {
        console.error('Error decoding token:', error);
      }
    } else {
      console.error('No token found in localStorage');
    }
  }, []);

  const setLoadingState = (role, isLoading) => {
    setLoadingStates((prev) => ({ ...prev, [role]: isLoading }));
  };

  const fetchManagerInfoByRole = async (role, userId) => {
    const roleUrlMap = {
      college_manager: `${BASE_URL}/college/${userId}`,
      school_manager: `${BASE_URL}/school/${userId}`,
    };

    const url = roleUrlMap[role];
    if (!url) {
      console.error('Unknown role');
      return;
    }

    setLoadingState(role, true);
    try {
      const response = await axios.get(url);
      if (role === 'college_manager') {
        setCollegeManagerInfo(response.data);
      } else if (role === 'school_manager') {
        setSchoolManagerInfo(response.data);
      }
    } catch (error) {
      setError(`${role} 정보를 불러오지 못했습니다.`);
    } finally {
      setLoadingState(role, false);
    }
  };

  return (
    <div>
      <h1>매니저 정보</h1>
      {Object.values(loadingStates).some((isLoading) => isLoading) && <p>Loading...</p>}

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {collegeManagerInfo && (
        <div>
          <h2>College Manager Info</h2>
          <p>Address: {collegeManagerInfo.address || '정보 없음'}</p>
          <p>Office Number: {collegeManagerInfo.officeNumber || '정보 없음'}</p>
          <p>College: {collegeManagerInfo.college || '정보 없음'}</p>
        </div>
      )}

      {schoolManagerInfo && (
        <div>
          <h2>School Manager Info</h2>
          <p>Address: {schoolManagerInfo.address || '정보 없음'}</p>
          <p>Office Number: {schoolManagerInfo.officeNumber || '정보 없음'}</p>
          <p>School: {schoolManagerInfo.schoolName || '정보 없음'}</p>
        </div>
      )}

      {!collegeManagerInfo && !schoolManagerInfo && !Object.values(loadingStates).some((isLoading) => isLoading) && (
        <p>해당하는 매니저 정보를 불러올 수 없습니다.</p>
      )}
    </div>
  );
};

export default ManagerInfo;
