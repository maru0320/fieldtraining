import React, { useState } from "react";
import './GeneralJoin.css';
import StudentJoin from "./generaljoin/StudentJoin";
import TeacherJoin from "./generaljoin/TeacherJoin";
import ProfessorJoin from "./generaljoin/ProfessorJoin";
import axios from 'axios';

function GeneralJoin() {
    const initialFormData = {
        userId: '',
        password: '',
        passwordCheck: '',
        name: '',
        email: '',
        college: '',
        department: '',
        studentNumber: '',
        phoneNumber: '',
        schoolName: '',
        subject: '',
        officeNumber: ''
    };
    const [value, setValue] = useState('');
    const [formData, setFormData] = useState({});
    const [error, setError] = useState('');
    const [resetFormSignal, setResetFormSignal] = useState(false); 
    

   // 가입 유형 변경
   const handleChange = (e) => {
    setValue(e.target.value);
};


    // 선택된 가입 유형에 맞는 폼을 렌더링
    function partChoice() {
        const commonData = {
            role: value,
            isApproval: false
        };

        if (value === 'student') {
            return <StudentJoin  />;
        } else if (value === 'teacher') {
            return <TeacherJoin />;
        } else if (value === 'professor') {
            return <ProfessorJoin />;
        }
        return null;
    }

    return (
        <div className="general-join-wrapper">
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


            </div>

        </div>
    );
}

export default GeneralJoin;
