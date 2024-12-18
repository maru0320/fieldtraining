import React, { useState, useEffect } from "react";
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
import { useNavigate } from "react-router-dom";

const BASE_URL = "http://localhost:8090/api/managerInfo";

const ManagerInfoModify = () => {
    const navigate = useNavigate();
    const [managerInfo, setManagerInfo] = useState({
        userId: "",
        password: "",
        confirmPassword: "",
        officeNumber: "",
        address: "",
        collegeName: "",
        schoolName: "",
    });

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const [isSubmitDisabled, setIsSubmitDisabled] = useState(true); // 버튼 활성화 상태
    const [role, setRole] = useState(""); // 역할 상태 추가

    useEffect(() => {
        const fetchManagerInfo = async () => {
            try{
                const token = localStorage.getItem('token');
                if (!token) {
                    throw new Error('로그인 정보가 없습니다.');
                }

                const decodedToken = jwtDecode(token);
                const userId = decodedToken.userId;
                const roles = decodedToken.roles;
                const sub = decodedToken.sub;

                // 역할 상태 설정
                setRole(roles.includes("ROLE_COLLEGE_MANAGER") ? "ROLE_COLLEGE_MANAGER" : "ROLE_SCHOOL_MANAGER");

                // 배열을 순회하며 URL을 동적으로 설정
                let url = '';
                if (roles.includes("ROLE_COLLEGE_MANAGER")) {
                    url = `${BASE_URL}/college/edit/${userId}`;
                } else if (roles.includes("ROLE_SCHOOL_MANAGER")) {
                    url = `${BASE_URL}/school/edit/${userId}`;
                } else {
                    throw new Error('지원되지 않는 역할입니다.');
                }

                const response = await axios.get(url);
                setManagerInfo({
                    ...managerInfo,
                    sub,
                    address: response.data.address,
                    officeNumber: response.data.officeNumber,
                    schoolName: response.data.schoolName,
                    college: response.data.college,
                });
            } catch(err) {
                console.error('매니저 정보 불러오기 오류:', err);
                setError(err.message || '매니저 정보를 불러오는 중 오류가 발생했습니다.');
            } finally {
                setLoading(false);
            }
        };
        fetchManagerInfo();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setManagerInfo((prevInfo) => ({
            ...prevInfo,
            [name]: value,
        }));
    };

    //수정 요청
    const handlePasswordCheck = () => {
        const { password, confirmPassword } = managerInfo;
        if (password === confirmPassword && password.trim() !== "") {
            setIsSubmitDisabled(false);
            setError(""); // 에러 메시지 제거
        } else {
            setIsSubmitDisabled(true);
            setError("비밀번호가 일치하지 않거나 비어 있습니다.");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const token = localStorage.getItem('token');

            const decodedToken = jwtDecode(token);
            const userId = decodedToken.userId;
            const roles = decodedToken.roles;

            // 배열을 순회하며 URL을 동적으로 설정
            let url = '';
            if (roles.includes("ROLE_COLLEGE_MANAGER")) {
                url = `${BASE_URL}/college/${userId}`;
            } else if (roles.includes("ROLE_SCHOOL_MANAGER")) {
                url = `${BASE_URL}/school/${userId}`;
            } else {
                throw new Error('지원되지 않는 역할입니다.');
            }

            // PUT 요청으로 수정된 데이터 서버로 전송
            await axios.put(url, managerInfo);

            alert('정보가 성공적으로 업데이트되었습니다.');
            navigate("/managerInfo");
        } catch (err) {
            setError(err.message || '정보 업데이트 중 오류가 발생했습니다.');
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p style={{ color: 'red' }}>Error: {error}</p>;
    
    return (
        <div>
            <h1>회원정보 수정</h1>
            <form onSubmit={handleSubmit}>
                <div id="Id">
                    <label>ID: </label>
                    <span>{managerInfo.sub}</span>
                </div>
                <div>
                    <label>신규 비밀번호 </label>
                    <input
                        type="password"
                        name="password"
                        value={managerInfo.password}
                        onChange={handleChange}
                        placeholder="새 비밀번호"
                    />
                </div>
                <div>
                    <label>비밀번호 확인 </label>
                    <input
                        type="password"
                        name="confirmPassword"
                        value={managerInfo.confirmPassword}
                        onChange={handleChange}
                        placeholder="비밀번호 확인"
                    />
                    <button type="button" onClick={handlePasswordCheck}>
                        확인
                    </button>
                </div>
                {error && <div style={{ color: "red" }}>{error}</div>}
                <div id="officePhone">
                    <label>사무실 번호 </label>
                    <input 
                        type="text"
                        name="officeNumber"
                        value={managerInfo.officeNumber} // 불러온 값이 표시됨
                        onChange={handleChange}
                        placeholder="예 : 02-1234-1234"
                    />
                </div>
                <div>
                    <label>주소 </label>
                    <input 
                        type="text"
                        name="address"
                        value={managerInfo.address} // 불러온 값이 표시됨
                        onChange={handleChange}
                        placeholder="주소를 입력하세요"
                    />
                </div>
                    {role === "ROLE_SCHOOL_MANAGER" && (
                        <div>
                            <label>학교 이름 </label>
                            <input
                                type="text"
                                name="schoolName"
                                value={managerInfo.schoolName || ""} // 불러온 값이 표시됨
                                onChange={handleChange}
                                placeholder="학교 이름"
                            />
                        </div>
                    )}
                    {role === "ROLE_COLLEGE_MANAGER" && (
                        <div>
                            <label>대학 이름 </label>
                            <input
                                type="text"
                                name="college"
                                value={managerInfo.college || ""} // 불러온 값이 표시됨
                                onChange={handleChange}
                                placeholder="대학 이름"
                            />
                        </div>
                    )}
                <button type="submit" disabled={isSubmitDisabled}>
                    정보 수정
                </button>
            </form>
        </div>
    );
};

export default ManagerInfoModify;