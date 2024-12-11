import React, { useState, useEffect } from "react";
import axios from "axios";
import {jwtDecode} from 'jwt-decode';

const ManagerInfoModify = () => {
  const [userData, setUserData] = useState({
    id: "",
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    phoneNumber: {
      firstPart: "010",
      secondPart: "",
      thirdPart: "",
    },
    officePhoneNumber: {
      firstPart: "02",
      secondPart: "",
      thirdPart: "",
    },
  });

  const [error, setError] = useState("");

  // 서버에서 사용자 정보 불러오기
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const token = localStorage.getItem("token");
        const decodedToken = token ? JSON.parse(atob(token.split(".")[1])) : {};
        const response = await axios.get(
          `http://localhost:8090/api/managerInfo/${decodedToken.userId}`
        );

        setUserData((prev) => ({
          ...prev,
          id: decodedToken.userId,
          name: response.data.name || "",
          email: `${response.data.email}@naver.com` || "",
          phoneNumber: {
            firstPart: response.data.phoneNumber?.split("-")[0] || "010",
            secondPart: response.data.phoneNumber?.split("-")[1] || "",
            thirdPart: response.data.phoneNumber?.split("-")[2] || "",
          },
          officePhoneNumber: {
            firstPart: response.data.officePhoneNumber?.split("-")[0] || "02",
            secondPart: response.data.officePhoneNumber?.split("-")[1] || "",
            thirdPart: response.data.officePhoneNumber?.split("-")[2] || "",
          },
        }));
      } catch (error) {
        console.error("Error loading user data", error);
        setError("사용자 정보를 불러오는 중 문제가 발생했습니다.");
      }
    };

    fetchUserInfo();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handlePhoneNumberChange = (e) => {
    const { name, value } = e.target;
    if (!/^\d*$/.test(value)) return; // 숫자만 입력 허용
    setUserData((prev) => ({
      ...prev,
      phoneNumber: {
        ...prev.phoneNumber,
        [name]: value,
      },
    }));
  };

  const handleOfficePhoneNumberChange = (e) => {
    const { name, value } = e.target;
    if (!/^\d*$/.test(value)) return; // 숫자만 입력 허용
    setUserData((prev) => ({
      ...prev,
      officePhoneNumber: {
        ...prev.officePhoneNumber,
        [name]: value,
      },
    }));
  };

  const handleSubmit = async () => {
    if (userData.password !== userData.confirmPassword) {
      setError("비밀번호와 확인 비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      const response = await axios.put(
        "http://localhost:8090/api/managerInfo",
        {
          id: userData.id,
          name: userData.name,
          phoneNumber: `${userData.phoneNumber.firstPart}-${userData.phoneNumber.secondPart}-${userData.phoneNumber.thirdPart}`,
          officePhoneNumber: `${userData.officePhoneNumber.firstPart}-${userData.officePhoneNumber.secondPart}-${userData.officePhoneNumber.thirdPart}`,
          password: userData.password,
        }
      );

      if (response.status === 200) {
        setError("수정 완료!");
      } else {
        setError("정보 수정 중 문제가 발생했습니다.");
      }
    } catch (error) {
      console.error("API Error", error);
      setError("정보 수정 중 문제가 발생했습니다.");
    }
  };

  return (
    <div>
      <h1>정보 수정</h1>
      {error && <div style={{ color: "red" }}>{error}</div>}
      <form>
        {/* 이름 */}
        <div>
          <label>이름 </label>
          <input
            type="text"
            name="name"
            value={userData.name}
            onChange={handleInputChange}
          />
        </div>

        {/* 이메일 */}
        <div>
          <label>이메일 </label>
          <input
            type="text"
            name="email"
            value={userData.email}
            onChange={handleInputChange}
          />
        </div>

        {/* 비밀번호 */}
        <div>
          <label>비밀번호 </label>
          <input
            type="password"
            name="password"
            value={userData.password}
            onChange={handleInputChange}
          />
        </div>
        <div>
          <label>비밀번호 확인 </label>
          <input
            type="password"
            name="confirmPassword"
            value={userData.confirmPassword}
            onChange={handleInputChange}
          />
        </div>

        {/* 전화번호 */}
        <div>
          <label>휴대폰 번호 </label>
          <input
            type="text"
            name="firstPart"
            value={userData.phoneNumber.firstPart}
            onChange={handlePhoneNumberChange}
          />
          <span>-</span>
          <input
            type="text"
            name="secondPart"
            value={userData.phoneNumber.secondPart}
            onChange={handlePhoneNumberChange}
          />
          <span>-</span>
          <input
            type="text"
            name="thirdPart"
            value={userData.phoneNumber.thirdPart}
            onChange={handlePhoneNumberChange}
          />
        </div>

        {/* 사무실 전화번호 */}
        <div>
          <label>사무실 번호 </label>
          <input
            type="text"
            name="firstPart"
            value={userData.officePhoneNumber.firstPart}
            onChange={handleOfficePhoneNumberChange}
          />
          <span>-</span>
          <input
            type="text"
            name="secondPart"
            value={userData.officePhoneNumber.secondPart}
            onChange={handleOfficePhoneNumberChange}
          />
          <span>-</span>
          <input
            type="text"
            name="thirdPart"
            value={userData.officePhoneNumber.thirdPart}
            onChange={handleOfficePhoneNumberChange}
          />
        </div>
        <button type="button" onClick={handleSubmit}>
          수정
        </button>
      </form>
    </div>
  );
};

export default ManagerInfoModify;
