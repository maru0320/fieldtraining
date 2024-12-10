import React, { useState } from "react";
import SubjectInput from "./SubjectInput";

function StudentJoin({ onChange, commonData }) {
    const [formData, setFormData] = useState({
        userId: '',
        password: '',
        passwordCheck: '',
        name: '',
        email: '',
        studentNumber: '',
        phoneNumber: '',
        schoolName: '',
        subject: '',
        college: '',
        department: '',
        role: commonData.role,  // 부모로부터 받은 role 값
        isApproval: commonData.isApproval  // 부모로부터 받은 isApproval 값
    });


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => {
            const newFormData = { ...prevData, [name]: value };
            onChange(newFormData);  // 부모 컴포넌트로 데이터 전달
            return newFormData;
        });
    };


    // const validate = () => {
    //     const errors = {};
    //     if (!formData.userId) errors.userId = '아이디를 입력해주세요.';
    //     if (!formData.password) errors.password = '비밀번호를 입력해주세요.';
    //     if (formData.password !== formData.passwordCheck) errors.passwordCheck = '비밀번호가 일치하지 않습니다.';
    //     if (!formData.email) errors.email = '이메일을 입력해주세요.';
    //     if (!formData.studentNumber) errors.studentNumber = '학번을 입력해주세요.';
    //     setValidationErrors(errors);
    //     return Object.keys(errors).length === 0;
    // };

    return (
        <div>

            <input
                className="inputbox"
                type="text"
                name="userId"
                placeholder="아이디를 입력해 주세요"
                value={formData.userId}
                onChange={handleChange}
            />
            <br />
            <SubjectInput onChange={handleChange} value={formData.subject} />
            <br />
            <input
                className="inputbox"
                type="password"
                name="password"
                placeholder="비밀번호"
                value={formData.password}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="password"
                name="passwordCheck"
                placeholder="비밀번호 확인"
                value={formData.passwordCheck}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="text"
                name="name"
                placeholder="이름"
                value={formData.name}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="text"
                name="studentNumber"
                placeholder="학번"
                value={formData.studentNumber}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="email"
                name="email"
                placeholder="이메일주소"
                value={formData.email}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="tel"
                name="phoneNumber"
                placeholder="휴대전화번호"
                value={formData.phoneNumber}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="text"
                name="schoolName"
                placeholder="실습협력학교명"
                value={formData.schoolName}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="text"
                name="college"
                placeholder="대학교"
                value={formData.college}
                onChange={handleChange}
            />
            <br />
            <input
                className="inputbox"
                type="text"
                name="department"
                placeholder="학과"
                value={formData.department}
                onChange={handleChange}
            />

            {/* <button type="button" onClick={validate}>
                유효성 검사 실행
            </button> */}
        </div>
    );
}

export default StudentJoin;
