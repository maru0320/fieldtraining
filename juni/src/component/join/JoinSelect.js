import React from 'react';
import { useNavigate } from "react-router-dom";
import './JoinSelect.css';

// 이미지 경로 수정
import personImage from '../../img/person.png'; // 기관 회원가입 이미지
import person2Image from '../../img/person2.png'; // 일반 회원가입 이미지

function JoinSelect() {
    const navigate = useNavigate();

    const onClickGeneralJoin = () => {
        navigate("/generaljoin");
    };

    const onClickInstitutionalJoin = () => {
        navigate("/institutionaljoin");
    };

    return (
        <div>
            <h1>가입하실 회원 유형을 선택해주세요</h1>
        <div className="container">
            

            <div className="buttonContainer">
                <button className="partButton" onClick={onClickGeneralJoin}>
                    <img src={person2Image} alt="일반 회원가입" className="buttonImage" />
                    <div className="buttonText">
                        일반 회원가입
                        <p>교육실습생, 실습협력학교(담당교사), 교원양성기관(교수)</p>
                    </div>
                </button>

                <button className="partButton" onClick={onClickInstitutionalJoin}>
                    <img src={personImage} alt="기관 회원가입" className="buttonImage" />
                    <div className="buttonText">
                        기관 회원가입
                        <p>실습협력학교 관리자, 교원양성기관 관리자</p>
                    </div>
                </button>
            </div>
        </div>
        </div>
    );
}

export default JoinSelect;