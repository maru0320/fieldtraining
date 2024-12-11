import React, { useState } from "react";
import './GeneralJoin.css';
import StudentJoin from "./generaljoin/StudentJoin";
import TeacherJoin from "./generaljoin/TeacherJoin";
import ProfessorJoin from "./generaljoin/ProfessorJoin";
import axios from 'axios';

function GeneralJoin() {
    const [value, setValue] = useState('');
    const [formData, setFormData] = useState({});

    const [error, setError] = useState('');
    
    const handleChange = (e) => {
        setValue(e.target.value);
    };

    const validateForm = () => {
        setError('');

        if (!formData.userId || !formData.password || !formData.name || !formData.email) {
            setError('모든 필드를 채워주세요.');
            return false;
        }

        if (formData.password !== formData.passwordCheck) {
            setError('비밀번호가 일치하지 않습니다.');
            return false;
        }

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(formData.email)) {
            setError('유효한 이메일 주소를 입력해주세요.');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // 폼 제출 기본 동작을 막고, 자체적으로 처리
    
        if (!validateForm()) {
            return;
        }

        try {
            // 데이터 전송을 위한 요청 시작
            const response = await axios.post('http://localhost:8090/generaljoin', {
                userId: formData.userId,
                password: formData.password,
                isApproval: false,  // 기본 값 설정
                role: value,        // 선택된 역할
                name: formData.name,
                email: formData.email,
                college: formData.college,
                department: formData.department,
                studentNumber: Number(formData.studentNumber),  // 숫자 타입으로 변환
                phoneNumber: formData.phoneNumber,
                schoolName: formData.schoolName,
                subject: formData.subject,
                officeNumber: formData.officeNumber
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // 성공적으로 응답이 오면
            if (response.status === 200) {
                alert("회원가입 성공!");
                setFormData({});
            } else {
                setError('회원가입에 실패했습니다. 응답 상태: ' + response.status);
            }
        } catch (error) {
            console.error("오류 발생:", error);
            setError('회원가입 중 오류가 발생했습니다. 오류: ' + error.message);
        }
    };

    // 선택된 가입 유형에 맞는 폼을 렌더링
    function partChoice() {
        const commonData = {
            role: value,
            isApproval: false
        };

        if (value === 'student') {
            return <StudentJoin onChange={setFormData} commonData={commonData} />;
        } else if (value === 'teacher') {
            return <TeacherJoin onChange={setFormData} commonData={commonData} />;
        } else if (value === 'professor') {
            return <ProfessorJoin onChange={setFormData} commonData={commonData} />;
        }
        return null;
    }

    return (
        <form onSubmit={handleSubmit}>
            <h2>일반 회원가입</h2>
            <div className="wrapper">
                <div className="part_wrapper">
                    <div className="part">
                        <input
                            type="radio"
                            name="part"
                            value="student"
                            onChange={handleChange}
                            id="student"
                        />
                        <label htmlFor="student">교육실습생</label>
                    </div>
                    <div className="part">
                        <input
                            type="radio"
                            name="part"
                            value="teacher"
                            onChange={handleChange}
                            id="teacher"
                        />
                        <label htmlFor="teacher">실습협력학교</label>
                    </div>
                    <div className="part">
                        <input
                            type="radio"
                            name="part"
                            value="professor"
                            onChange={handleChange}
                            id="professor"
                        />
                        <label htmlFor="professor">교육양성기관</label>
                    </div>
                </div>

                {/* 선택된 폼 렌더링 */}
                <div>
                    {partChoice()}
                </div>


            {/* 회원가입 버튼 */}
            <div>
                <button className="submitButton" type="submit">회원가입</button>
            </div>



            </div>

        </form>
    );
}

export default GeneralJoin;