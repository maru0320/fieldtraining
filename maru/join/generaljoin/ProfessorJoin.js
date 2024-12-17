import React, { useState, useEffect } from "react";
import axios from "axios";

function ProfessorJoin({}) {
    const [formData, setFormData] = useState({
        userId: '',
        password: '',
        passwordCheck: '',
        name: '',
        email: '',
        phoneNumber: '',
        officeNumber: '',
        college: '',
        department: '',
        studentNumber: '',
        schoolName: '',
        subject: ''
    });

    const [errorMessages, setErrorMessages] = useState({});
    const [inputStyles, setInputStyles] = useState({});
    const [isDuplicateChecked, setIsDuplicateChecked] = useState(false);
    const [duplicateMessage, setDuplicateMessage] = useState('');
    const [messageColor, setMessageColor] = useState('');
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };


    // 유효성 검사
    const validateForm = () => {
        const errors = {};
        const styles = {};
        let isValid = true;

        if (!formData.userId) {
            errors.userId = "아이디를 입력해주세요.";
            styles.userId = "red";
            isValid = false;
        }
        if (!formData.password) {
            errors.password = "비밀번호를 입력해주세요.";
            styles.password = "red";
            isValid = false;
        }else if (formData.password !== formData.passwordCheck) {
            errors.password = "비밀번호가 일치하지 않습니다.";
            styles.password = "red";
            isValid = false;
        } else {
            styles.password = '#7ac142';
        }
        if (!formData.name) {
            errors.name = "이름을 입력해주세요.";
            styles.name = "red";
            isValid = false;
        } else {
            styles.name = '#7ac142';
        }
        if (!formData.email) {
            errors.email = "이메일을 입력해주세요.";
            styles.email = "red";
            isValid = false;
        }else {
            styles.email = '#7ac142';
        }
        if (!formData.phoneNumber) {
            errors.phoneNumber = "휴대전화번호를 입력해주세요.";
            styles.phoneNumber = "red";
            isValid = false;
        }else {
            styles.phoneNumber = '#7ac142';
        }
        if (!formData.officeNumber) {
            errors.officeNumber = "사무실 번호를 입력해주세요.";
            styles.officeNumber = "red";
            isValid = false;
        }else {
            styles.officeNumber = '#7ac142';
        }
        if (!formData.college) {
            errors.college = "대학교를 입력해주세요.";
            styles.college = "red";
            isValid = false;
        }else {
            styles.college = '#7ac142';
        }
        if (!formData.department) {
            errors.department = "학과를 입력해주세요.";
            styles.department = "red";
            isValid = false;
        }else {
            styles.department = '#7ac142';
        }

        setErrorMessages(errors);
        setInputStyles(styles);
        return isValid;
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

    const handleSubmit = async (e) => {

        e.preventDefault(); // 폼 제출 기본 동작을 막고, 자체적으로 처리
    
        if (!validateForm()) {
            return;
        }

        // 중복 확인 여부 검사
        if (!isDuplicateChecked) {
            alert('아이디 중복 확인을 해주세요.');
            return;
        }
        

        try {
            // 데이터 전송을 위한 요청 시작
            const response = await axios.post('http://localhost:8090/join/generaljoin', {
                userId: formData.userId,
                password: formData.password,
                isApproval: false,  // 기본 값 설정
                role: 'professor',        // 선택된 역할
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
            if (response.status === 200 || response.status === 201) {
                 alert("회원가입 성공!");
                 setFormData({
                 userId: '',
                 password: '',
                 passwordCheck: '',
                 name: '',
                 email: '',
                 phoneNumber: '',
                 officeNumber: '',
                 college: '',
                 department: '',
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
            <div>
                {/* 아이디 입력 */}
                <input
                    className="inputbox"
                    type="text"
                    name="userId"
                    placeholder="아이디를 입력해 주세요"
                    value={formData.userId}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.userId }}
                />
                {errorMessages.userId && <p style={{ color: "red" }}>{errorMessages.userId}</p>}
                <button className="duplicateCheckButton" onClick={duplicateCheck}>
                    중복체크
                </button>
                {duplicateMessage && <p style={{ color: messageColor }}>{duplicateMessage}</p>}
                <br/>
                {/* 비밀번호 입력 */}
                <input
                    className="inputbox"
                    type="password"
                    name="password"
                    placeholder="비밀번호"
                    value={formData.password}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.password }}
                />
                
                <br/>
                {/* 비밀번호 확인 */}
                <input
                    className="inputbox"
                    type="password"
                    name="passwordCheck"
                    placeholder="비밀번호 확인"
                    value={formData.passwordCheck}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.password }}
                />
                {errorMessages.passwordCheck && (
                    <p style={{ color: "red" }}>{errorMessages.passwordCheck}</p>
                )}
                {errorMessages.password && <p style={{ color: "red" }}>{errorMessages.password}</p>}
                <br/>
                {/* 이름 입력 */}
                <input
                    className="inputbox"
                    type="text"
                    name="name"
                    placeholder="이름"
                    value={formData.name}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.name }}
                />
                {errorMessages.name && <p style={{ color: "red" }}>{errorMessages.name}</p>}
                <br/>
                {/* 이메일 입력 */}
                <input
                    className="inputbox"
                    type="email"
                    name="email"
                    placeholder="이메일주소"
                    value={formData.email}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.email }}
                />
                {errorMessages.email && <p style={{ color: "red" }}>{errorMessages.email}</p>}
                <br/>
                {/* 휴대전화번호 입력 */}
                <input
                    className="inputbox"
                    type="tel"
                    name="phoneNumber"
                    placeholder="휴대전화번호"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.phoneNumber }}
                />
                {errorMessages.phoneNumber && <p style={{ color: "red" }}>{errorMessages.phoneNumber}</p>}
                <br/>
                {/* 사무실 번호 입력 */}
                <input
                    className="inputbox"
                    type="text"
                    name="officeNumber"
                    placeholder="사무실 번호"
                    value={formData.officeNumber}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.officeNumber }}
                />
                {errorMessages.officeNumber && <p style={{ color: "red" }}>{errorMessages.officeNumber}</p>}
                <br/>
                {/* 대학교 입력 */}
                <input
                    className="inputbox"
                    type="text"
                    name="college"
                    placeholder="대학교"
                    value={formData.college}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.college }}
                />
                {errorMessages.college && <p style={{ color: "red" }}>{errorMessages.college}</p>}
                <br/>
                {/* 학과 입력 */}
                <input
                    className="inputbox"
                    type="text"
                    name="department"
                    placeholder="학과"
                    value={formData.department}
                    onChange={handleChange}
                    style={{ borderColor: inputStyles.department }}
                />
                {errorMessages.department && <p style={{ color: "red" }}>{errorMessages.department}</p>}

                {/* 제출 버튼 */}
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

export default ProfessorJoin;
