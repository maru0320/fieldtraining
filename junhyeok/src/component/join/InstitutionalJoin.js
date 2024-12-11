import './InstitutionalJoin.css';
import UploadBox from '../UploadBox';
import axios from 'axios';
import React, { useState } from "react";

function InstitutionalJoin() {
    const [formData, setFormData] = useState({
        userId: '',
        password: '',
        passwordCheck: '',
        officeNumber: '',
        address: '',
        managerId: '',
        role: '',
        isApproval: '',
        proofData: null, // 파일 정보 추가
    });

    const [value, setValue] = useState('');
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    const handleRoleChange = (e) => {
        setValue(e.target.value);
    };

    const handleFileUpload = (file) => {
        setFormData((prevData) => ({ ...prevData, proofData: file })); // 'proofData' 필드로 파일 저장
    };

    const validateForm = () => {
        setError('');
    
        if (!formData.userId || !formData.password || !formData.officeNumber) {
            setError('모든 필드를 채워주세요.');
            console.error("유효성 검사 실패: 필드 누락");
            return false;
        }
    
        if (formData.password !== formData.passwordCheck) {
            setError('비밀번호가 일치하지 않습니다.');
            console.error("유효성 검사 실패: 비밀번호 불일치");
            return false;
        }
    
        console.log("유효성 검사 통과");
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        if (!validateForm()) return;
    
        const formDataToSend = new FormData();
        formDataToSend.append('userId', formData.userId);
        formDataToSend.append('password', formData.password);
        formDataToSend.append('role', value);
        formDataToSend.append('address', formData.address);
        formDataToSend.append('managerId', formData.managerId);
        formDataToSend.append('officeNumber', formData.officeNumber);
        formDataToSend.append('isApproval', false);  // 기본 값 설정
    
        // 파일이 존재하면 추가
        if (formData.proofData) {
            formDataToSend.append('proofData', formData.proofData);
        }
    
        try {
            // `headers`에서 `Content-Type`을 설정하지 않음
            const response = await axios.post('http://localhost:8090/join/institutionaljoin', formDataToSend, {
                // 헤더를 명시적으로 설정할 필요 없음
                headers: {
                    // 자동으로 설정되는 multipart/form-data를 신경 쓸 필요 없음
                    'Accept': 'application/json',
                }
            });
            if (response.status === 200 || response.status === 201) {
                alert("회원가입 성공!");
                setFormData({
                    userId: '',
                    password: '',
                    passwordCheck: '',
                    officeNumber: '',
                    address: '',
                    managerId: '',
                    role: '',
                    isApproval: '',
                    proofData: null // 파일 초기화
                });
            } else {
                setError('회원가입에 실패했습니다. 응답 상태: ' + response.status);
            }
        } catch (error) {
            console.error("오류 발생:", error);
            setError('회원가입 중 오류가 발생했습니다. 오류: ' + error.message);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>기관 회원가입</h2>
            <div className="wrapper">
                <div className="part_wrapper">
                    <div className="part">
                        <input
                            type="radio"
                            name="manage_part"
                            value="schoolManager"
                            onChange={handleRoleChange}
                            id="teacher"
                        />
                        <label htmlFor="teacher">실습협력학교</label>
                    </div>
                    <div className="part">
                        <input
                            type="radio"
                            name="manage_part"
                            value="collegeManager"
                            onChange={handleRoleChange}
                            id="professor"
                        />
                        <label htmlFor="professor">교육양성기관</label>
                    </div>
                </div>
                <input
                    className="inputbox"
                    type="text"
                    name="userId"
                    placeholder="아이디를 입력해 주세요"
                    value={formData.userId}
                    onChange={handleChange}
                />

                <input 
                    className="inputbox" 
                    type="password" 
                    name="password" 
                    placeholder="비밀번호" 
                    value={formData.password}
                    onChange={handleChange}
                />
             
                <input 
                    className="inputbox" 
                    type="password" 
                    name="passwordCheck" 
                    placeholder="비밀번호 확인"
                    value={formData.passwordCheck}
                    onChange={handleChange}
                />
                
                <input 
                    className="inputbox" 
                    type="tel" 
                    name="officeNumber" 
                    placeholder="대표전화번호" 
                    value={formData.officeNumber}
                    onChange={handleChange}
                />
              
                <input 
                    className="inputbox" 
                    type="text" 
                    name="address" 
                    placeholder="주소"
                    value={formData.address}
                    onChange={handleChange}
                />

                {/* UploadBox 컴포넌트 추가 */}
                <UploadBox onUpload={handleFileUpload} value={formData.proofData} />
          
                <input 
                    className="inputbox" 
                    type="text" 
                    name="managerId" 
                    placeholder="담당자ID" 
                    value={formData.managerId}
                    onChange={handleChange}
                />
                {error && <p className="error-message">{error}</p>}
                <div>
                    <button className="submitButton" type="submit">회원가입</button>
                </div>
            </div>
        </form>
    );
}

export default InstitutionalJoin;