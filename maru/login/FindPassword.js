import axiosInstance from "../axiosInstance";
import React, { useState } from "react";
import axios from "axios";

function FindPassword() {
    const [userId, setUserId] = useState('');
    const [email, setEmail] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [isUserVerified, setIsUserVerified] = useState(false); // 사용자 확인 여부

    

    // 사용자 확인 요청
    const handleVerifyUser = async (e) => {
        e.preventDefault();
        try {
            // 사용자 확인 요청
            const response = await axiosInstance.post('/find/verify-user', {
                userId,
                email,
            });
            // 사용자 확인 성공
            if (response.data.success) {
                setMessage("사용자가 확인되었습니다. 새 비밀번호를 입력하세요.");
                setIsUserVerified(true); // 사용자 확인 성공 시 비밀번호 입력 필드 표시
            } else {
                setMessage("사용자가 존재하지 않습니다.");
            }
        } catch (error) {
            if (error.response && error.response.data) {
                setMessage(error.response.data.message || "사용자 확인 중 오류가 발생했습니다."); // 서버의 에러 메시지
            } else {
                setMessage("사용자 확인 중 오류가 발생했습니다."); // 기타 오류
            }
        }
    };

    // 비밀번호 재설정 요청
    const handleResetPassword = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setError('비밀번호가 일치하지 않습니다.');
            return;
        }

        setError('');

        try {
            // 비밀번호 재설정 요청
            const response = await axiosInstance.post('/find/password', {
                userId,
                email,
                newPassword,
            });
            setMessage("비밀번호가 성공적으로 변경되었습니다.");
        } catch (error) {
            if (error.response && error.response.data) {
                setMessage(error.response.data.message || "비밀번호 재설정 중 오류가 발생했습니다."); // 서버의 에러 메시지
            } else {
                setMessage("비밀번호 재설정 중 오류가 발생했습니다."); // 기타 오류
            }
        }
    };

    return (
        <div className="loginWrapper">
            <h1>비밀번호 찾기</h1>
            {!isUserVerified ? (
                <form onSubmit={handleVerifyUser}>
                    <input
                        className="inputbox"
                        type="text"
                        name="userId"
                        value={userId}
                        placeholder="아이디"
                        onChange={(e) => setUserId(e.target.value)}
                    />
                    <br />
                    <input
                        className="inputbox"
                        type="email"
                        name="email"
                        value={email}
                        placeholder="이메일 주소"
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <br />
                    <button type="submit">사용자 확인</button>
                </form>
            ) : (
                <form onSubmit={handleResetPassword}>
                    <input
                        className="inputbox"
                        type="password"
                        name="newPassword"
                        value={newPassword}
                        placeholder="새 비밀번호"
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
                    <br />
                    <input
                        className="inputbox"
                        type="password"
                        name="confirmPassword"
                        value={confirmPassword}
                        placeholder="비밀번호 확인"
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                    <br />
                    {error && <p style={{ color: 'red' }}>{error}</p>} {/* 에러 메시지 표시 */}
                    <button type="submit">비밀번호 재설정</button>
                </form>
            )}
            {message && <p>{message}</p>} {/* 메시지 출력 */}
        </div>
    );
}

export default FindPassword;
