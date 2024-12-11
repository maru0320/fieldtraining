import React from 'react'
import { BrowserRouter, Routes, Route } from "react-router-dom"; // 이 구문을 넣으면 됨
import { useState } from 'react';
import Matching from './component/user/matching/Matching';
import Attendance from './component/user/attendance/Attendance';
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
import FaQBoard from './component/manager/faqboard/FaQBoard';
import FaQWrite from './component/manager/faqboard/FaQWrite';
import FormBoard from './component/manager/formboard/FormBoard';
import FormWrite from './component/manager/formboard/FormWrite';
import Notice from './component/manager/notice/Notice';
import NoticeWrite from './component/manager/notice/NoticeWrite';
import ReferenceBoard from './component/manager/referenceboard/ReferenceBoard';
import ReferenceWrite from './component/manager/referenceboard/ReferenceWrite';
import ListManagement from './component/manager/listmanagement/ListManagement';
import ManagerInfo from './component/manager/info/ManagerInfo';
import DepartmentSearch from './component/manager/listmanagement/DepartmentSearch';
import MatchCheck from './component/manager/matchingcheck/MatchCheck';
import ManagerInfoModify from './component/manager/info/ManagerInfoModify';
import Calendar from './component/manager/calendar/Calendar';
import Main from './component/Main';
import Footer from './component/Footer';

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
                <Route path="/" element={<Main />} />
                <Route path="/Maching" element={<Matching />} /> 
                <Route path="/Practice" element={<Practice posts={postArray} />} /> 
                <Route path="/PracticeWrite" element={<PracticeWrite postArray={postArray} setPostArray={setPostArray} />} /> 
                <Route path="/FaQBoard" element={<FaQBoard posts={postArray} />} /> 
                <Route path="/FaQWrite" element={<FaQWrite postArray={postArray} setPostArray={setPostArray} />} /> 
                <Route path="/FormBoard" element={<FormBoard posts={postArray} />} /> 
                <Route path="/FormWrite" element={<FormWrite postArray={postArray} setPostArray={setPostArray} />} /> 
                <Route path="/Notice" element={<Notice posts={postArray} />} /> 
                <Route path="/NoticeWrite" element={<NoticeWrite postArray={postArray} setPostArray={setPostArray} />} /> 
                <Route path="/ReferenceBoard" element={<ReferenceBoard posts={postArray} />} /> 
                <Route path="/ReferenceWrite" element={<ReferenceWrite postArray={postArray} setPostArray={setPostArray} />} /> 
                <Route path="/Class" element={<Class />} /> 
                <Route path="/Operation" element={<Operation />} /> 
                <Route path="/Union" element={<Union />} /> 
                <Route path="/Rating" element={<Rating />} /> 
                <Route path="/RatingCheck" element={<RatingCheck />} /> 
                <Route path="/Attendance" element={<Attendance />} /> 
                <Route path="/absent" element={<absent />} />  
                <Route path="/generaljoin" element={<GeneralJoin />} />
                <Route path="/institutionalJoin" element={<InstitutionalJoin />} />
                <Route path="/joinselect" element={<JoinSelect/>} />
                <Route path="/login" element={<Login/>} />
                <Route path="/findid" element={<FindID/>} />
                <Route path="/findpassword" element={<FindPassword/>} />
                <Route path='/listmanagement' element={<ListManagement />} />
                <Route path='/managerInfo' element={<ManagerInfo />} />
                <Route path='/managerInfoModify' element={<ManagerInfoModify />} />
                <Route path='/departmentSearch' element={<DepartmentSearch />} />
                <Route path='/matchCheck' element={<MatchCheck />} />
                <Route path='/calendar' element={<Calendar />} />
              </Routes>
            </div>
          </div>
        
        
      </div>
    </BrowserRouter>
    
  );
}


export default App;