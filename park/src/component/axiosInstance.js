import axios from 'axios';


// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8090', // 기본 URL 설정
  withCredentials : true,
  headers: {
    'Content-Type': 'application/json', // 기본 content-type 설정
  },
});

// 요청 인터셉터 추가
axiosInstance.interceptors.request.use(
  (config) => {

    // 로컬 스토리지에서 JWT 토큰을 가져와 Authorization 헤더에 추가
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
