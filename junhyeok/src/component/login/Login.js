import './Login.css';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import React, { useState } from "react";

function Login() {
    const [userid, setUserid] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        if (!userid || !password) {
            setError("아이디와 비밀번호를 입력해주세요.");
            return;
        }
        console.log("userid:", userid, "password:", password);
        setError('');
        setLoading(true);
        try {
            const response = await axios.post('http://localhost:8090/login', {
                userId: userid,
                password:password
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            const { token } = response.data;
            if (token) {
                localStorage.setItem('token', token); // 토큰을 localStorage에 저장

                window.dispatchEvent(new Event('storage'));

                navigate('/'); // 홈 화면으로 리다이렉트
            } else {
                setError("로그인 실패. 잠시 후 다시 시도해주세요.");
            }

        } catch (err) {
            if (err.response && err.response.status === 401) {
                setError("아이디 또는 비밀번호가 잘못되었습니다.");
            } else {
                setError("로그인 실패. 잠시 후 다시 시도해주세요.");
            }
        } finally {
            setLoading(false);
        }
    };

    const onClickFindID = () => {
        navigate("/findid");
    };

    const onClickFindPassword = () => {
        navigate("/findpassword");
    };

    return (
        <div className="loginWrapper">
            <h1>로그인</h1>
            <form onSubmit={handleLogin}>

                <input
                    className="inputbox"
                    type="text"
                    name="userid"
                    value={userid}
                    onChange={(e) => setUserid(e.target.value)}
                    placeholder="아이디"
                />
                <br></br>
                <input
                    className="inputbox"
                    type="password"
                    name="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="비밀번호"
                />
                <br></br>
                {error && <p className="errorMessage">{error}</p>} {/* 에러 메시지 표시 */}
                <button type="submit" disabled={loading}>
                    {loading ? '로그인 중...' : '로그인'}
                </button>
                <br></br>
            </form>
            <div className="loginButtonContainer">
                <button className="loginButton" onClick={onClickFindID}>아이디찾기</button>
                <button className="loginButton" onClick={onClickFindPassword}>비밀번호찾기</button>
            </div>
        </div>
    );
}

export default Login;