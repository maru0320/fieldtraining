import React, { useEffect } from 'react'
import { BrowserRouter, Routes, Route } from "react-router-dom"; // 이 구문을 넣으면 됨
import { useState } from 'react';
import Class from './component/user/class/Class';
import Operation from './component/user/operation/Operation';
import Practice from './component/user/practice/Practice';
import Rating from './component/user/rating/Rating';
import RatingCheck from './component/user/ratingcheck/RatingCheck';
import Union from './component/user/union/Union';
import PracticeWrite from './component/user/practice/PracticeWrite';
import GeneralJoin from "./component/join/GeneralJoin";
import InstitutionalJoin from "./component/join/InstitutionalJoin";
import Login from './component/login/Login';
import FindID from './component/login/FindID';
import FindPassword from './component/login/FindPassword';
import Header from './component/Header';
import JoinSelect from './component/join/JoinSelect';
import StudentPage from './component/user/matching/StudentPage';
import TeacherPage from './component/user/matching/TeacherPage';
import Footer from './component/Footer';
import MatchStatus from './component/user/matching/MatchStatusPage';
import MatchStatusPage from './component/user/matching/MatchStatusPage';
import PracticeUpdate from './component/user/practice/PracticeUpdate';
import BoardDetail from './component/user/practice/BoardDetail';
import TeacherAttendanceApproval from './component/user/attendance/TeacherAttendanceApproval';
import AttendanceStatusPage from './component/user/attendance/AttendanceStatusPage';
import TeacherMatchedStudentsAttendancePage from './component/user/attendance/TeacherMatchedStudentsAttendancePage';
import './global.css';  // 공통 스타일 임포트
import MyPage from './component/user/mypage/Mypage';

function App() {
  const [postArray, setPostArray] = useState([]);

  return (
    <BrowserRouter>
    <div>
      <Header />
      <div>
        <div>
        <Footer />

         <Routes>
          <Route path="/StudentPage" element={<StudentPage />} /> 
          <Route path="/TeacherPage" element={<TeacherPage />} />
          <Route path="/match-status" element={<MatchStatusPage />} />
          <Route path="/Practice" element={<Practice posts={postArray} />} /> 
          <Route path="/PracticeWrite" element={<PracticeWrite postArray={postArray} setPostArray={setPostArray} />} /> 
          <Route path="/PracticeUpdate" element={<PracticeUpdate />} />
          <Route path="/BoardDetail" element={<BoardDetail />} />
          <Route path="/Class" element={<Class />} /> 
          <Route path="/Operation" element={<Operation />} /> 
          <Route path="/Union" element={<Union />} /> 
          <Route path="/Rating" element={<Rating />} /> 
          <Route path="/RatingCheck" element={<RatingCheck />} /> 
          <Route path="/absent" element={<absent />} />  
          <Route path="/join/generaljoin" element={<GeneralJoin />} />
          <Route path="/join/institutionalJoin" element={<InstitutionalJoin />} />
          <Route path="/joinselect" element={<JoinSelect/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/findid" element={<FindID/>} />
          <Route path="/findpassword" element={<FindPassword/>} />
          <Route path="/MatchStatus" element={<MatchStatus />} />
          <Route path="/AttendanceStatus" element={<AttendanceStatusPage />} />
          <Route path="/TeacherAttendanceApproval" element={<TeacherAttendanceApproval />} />
          <Route path="/TeacherMatchedStudentsAttendancePage" element={<TeacherMatchedStudentsAttendancePage />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes> 
        </div>
      </div>
    </div>
    </BrowserRouter>  
  );
}


export default App;