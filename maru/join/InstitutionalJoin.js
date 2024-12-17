import './InstitutionalJoin.css';
import UploadBox from '../UploadBox';
import axios from 'axios';
import React, { useEffect, useState } from "react";
import AddressSearch from './AddressSearch';

function InstitutionalJoin() {
    const [isDuplicateChecked, setIsDuplicateChecked] = useState(false); // 중복 확인 여부
    const [duplicateMessage, setDuplicateMessage] = useState(''); // 중복 확인 메시지
    const [messageColor, setMessageColor] = useState(''); // 메시지 색상
    const [inputStyles, setInputStyles] = useState({
        userId: '', // 아이디 입력 스타일 초기값
        password: '',
        passwordCheck: '',
        officeNumber: '',
        address: '',
        managerId: '',
    });

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
    const [errorMessages, setErrorMessages] = useState({}); // 각 필드별 에러 메시지 관리

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    useEffect(() => {
        if (formData.userId) {
            setDuplicateMessage('중복확인을 해주세요.');
            setMessageColor('orange'); // 중복 확인 필요 시 메시지 색상
            setIsDuplicateChecked(false);
        } else {
            setDuplicateMessage(''); // 아이디 입력란이 비었을 때 메시지 초기화
        }
    }, [formData.userId]);

    const duplicateCheck = async (e) => {
        e.preventDefault();  // 중복 체크 버튼 클릭 시 폼 제출 방지
        if (!formData.userId) { // userId가 비어 있는 경우에만 메시지 표시
            setDuplicateMessage('아이디를 입력하세요.');
            setMessageColor('red');
            setInputStyles((prevStyles) => ({
                ...prevStyles,
                userId: 'red', // 아이디가 비어 있으면 빨간색
            }));
            return;
        }

        try {
            const response = await axios.get('http://localhost:8090/join/duplicateCheck', {
                params: { userId: formData.userId },
            });
            const newInputStyles = {};

            if (response.data) {
                setDuplicateMessage('이미 사용중인 아이디입니다.');
                setMessageColor('red');
                setIsDuplicateChecked(false);
                newInputStyles.userId = 'red'; // 중복된 경우 빨간색
            } else {
                setDuplicateMessage('사용 가능한 아이디입니다.');
                setMessageColor('blue');
                setIsDuplicateChecked(true);
                newInputStyles.userId = '#7ac142'; // 사용 가능한 아이디는 초록색
            }

            setInputStyles(newInputStyles); // 상태 업데이트
        } catch (error) {
            console.error('아이디 중복 확인 중 오류 발생:', error);
            setDuplicateMessage('아이디 중복 확인에 실패했습니다.');
            setMessageColor('red');
            setIsDuplicateChecked(false);
        }
    };

    const handleRoleChange = (e) => {
        setValue(e.target.value);
    };

    const handleFileUpload = (file) => {
        setFormData((prevData) => ({ ...prevData, proofData: file })); // 'proofData' 필드로 파일 저장
    };

    const validateForm = () => {
        const newErrorMessages = {}; // 새로운 에러 메시지를 저장할 객체
        let isValid = true;
        const newInputStyles = {}; // 각 입력 필드의 스타일을 관리할 객체

        if (!formData.password) {
            newErrorMessages.password = '비밀번호를 입력해주세요.';
            newInputStyles.password = 'red';
            isValid = false;
        } else {
            newInputStyles.password = '#7ac142';
        }

        if (!formData.passwordCheck) {
            newErrorMessages.passwordCheck = '비밀번호를 입력해주세요.';
            newInputStyles.passwordCheck = 'red';
            isValid = false;
        } else if (formData.password !== formData.passwordCheck) {
            newErrorMessages.passwordCheck = '비밀번호가 일치하지 않습니다.';
            newInputStyles.passwordCheck = 'red';
            isValid = false;
        } else {
            newInputStyles.passwordCheck = '#7ac142';
        }

        if (!formData.officeNumber) {
            newErrorMessages.officeNumber = '대표전화번호를 입력해주세요.';
            newInputStyles.officeNumber = 'red';
            isValid = false;
        } else {
            newInputStyles.officeNumber = '#7ac142';
        }

        if (!formData.address) {
            newErrorMessages.address = '주소를 입력해주세요.';
            newInputStyles.address = 'red';
            isValid = false;
        } else {
            newInputStyles.address = '#7ac142';
        }

        if (!formData.managerId) {
            newErrorMessages.managerId = '담당자 ID를 입력해주세요.';
            newInputStyles.managerId = 'red';
            isValid = false;
        } else {
            newInputStyles.managerId = '#7ac142';
        }

        if (!formData.proofData) {
            newErrorMessages.proofData = '증빙자료를 업로드해주세요.';
            isValid = false;
        }

        setErrorMessages(newErrorMessages); // 에러 메시지 상태 업데이트
        setInputStyles(newInputStyles); // 스타일 상태 업데이트
        return isValid;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 폼 전체 유효성 검사
        if (!validateForm()) {
            return;
        }

        // 중복 확인 여부 검사
        if (!isDuplicateChecked) {
            alert('아이디 중복 확인을 해주세요.');
            return;
        }

        // 폼 데이터 전송 로직
        const formDataToSend = new FormData();
        formDataToSend.append('userId', formData.userId);
        formDataToSend.append('password', formData.password);
        formDataToSend.append('role', value);
        formDataToSend.append('address', formData.address);
        formDataToSend.append('managerId', formData.managerId);
        formDataToSend.append('officeNumber', formData.officeNumber);
        formDataToSend.append('isApproval', false);

        // 파일 추가
        if (formData.proofData) {
            formDataToSend.append('proofData', formData.proofData);
        }

        try {
            const response = await axios.post('http://localhost:8090/join/institutionaljoin', formDataToSend, {
                headers: { 'Accept': 'application/json' },
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
                    proofData: null,
                });
            } else {
                setErrorMessages({ general: '회원가입에 실패했습니다. 응답 상태: ' + response.status });
            }
        } catch (error) {
            console.error("오류 발생:", error);
            setErrorMessages({ general: '회원가입 중 오류가 발생했습니다. 오류: ' + error.message });
        }
    };

    return (
        <form onSubmit={handleSubmit} className="institutional-join">
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
                    style={{
                        borderColor: inputStyles.userId, // 중복 체크 후 색상 적용
                    }}
                />
                {errorMessages.userId && <p style={{ color: 'red' }}>{errorMessages.userId}</p>}
                <button className='duplicateCheckButton' onClick={duplicateCheck}>중복체크</button>
                {duplicateMessage && <p style={{ color: messageColor }}>{duplicateMessage}</p>}
                <input
                    className="inputbox"
                    type="password"
                    name="password"
                    placeholder="비밀번호"
                    value={formData.password}
                    onChange={handleChange}
                    style={{
                        borderColor: inputStyles.passwordCheck,
                    }}
                />

                <input
                    className="inputbox"
                    type="password"
                    name="passwordCheck"
                    placeholder="비밀번호 확인"
                    value={formData.passwordCheck}
                    onChange={handleChange}
                    style={{
                        borderColor: inputStyles.passwordCheck,
                    }}
                />
                {errorMessages.passwordCheck && <p style={{ color: 'red' }}>{errorMessages.passwordCheck}</p>}
                <input
                    className="inputbox"
                    type="tel"
                    name="officeNumber"
                    placeholder="대표전화번호"
                    value={formData.officeNumber}
                    onChange={handleChange}
                    style={{
                        borderColor: inputStyles.officeNumber,
                    }}
                />
                {errorMessages.officeNumber && <p style={{ color: 'red' }}>{errorMessages.officeNumber}</p>}
                <AddressSearch
                    onAddressChange={(data) =>
                        setFormData((prevData) => ({
                            ...prevData,
                            address: `${data.postcode} ${data.address}`,
                        }))
                    }
                />
                <input
                    className="inputbox"
                    type="text"
                    name="address"
                    placeholder="주소"
                    value={formData.address}
                    readOnly
                    style={{
                        borderColor: inputStyles.address,
                    }}
                />
                {errorMessages.address && <p style={{ color: 'red' }}>{errorMessages.address}</p>}
                <UploadBox onUpload={handleFileUpload} value={formData.proofData} />
                {errorMessages.proofData && <p style={{ color: 'red' }}>{errorMessages.proofData}</p>}
                <input
                    className="inputbox"
                    type="text"
                    name="managerId"
                    placeholder="담당자ID"
                    value={formData.managerId}
                    onChange={handleChange}
                    style={{
                        borderColor: inputStyles.managerId,
                    }}
                />
                {errorMessages.managerId && <p style={{ color: 'red' }}>{errorMessages.managerId}</p>}
                {errorMessages.general && <p style={{ color: 'red' }}>{errorMessages.general}</p>}
                <div>
                    <button
                        className="submitButton"
                        type="submit"
                        disabled={!isDuplicateChecked}>
                        회원가입
                    </button>
                </div>
            </div>
        </form>
    );
}

export default InstitutionalJoin;
