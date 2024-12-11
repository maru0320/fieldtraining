import React from 'react';
import ReactDOM from 'react-dom/client';
import './global.css';  // 전역 스타일 임포트
import App from './App';
import axios from 'axios';

// axios 기본 설정
axios.defaults.withCredentials = true;  // 쿠키 전송 허용
axios.defaults.xsrfCookieName = 'XSRF-TOKEN';  // 쿠키에서 CSRF 토큰을 읽을 이름
axios.defaults.xsrfHeaderName = 'X-XSRF-TOKEN';  // 요청 헤더에 포함시킬 이름

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <App />
);

