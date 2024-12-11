import React from 'react';
import { Link } from 'react-router-dom';

import "./Main.css";

const Main = () => {
    return(
        <div className="Main">
            <div className="content">
                
            </div>
            <div className="groupBox">
                <div className="Box1">
                    <div>
                        <p>참고자료</p>
                        <Link to="/referenceBoard" className="text-link">더보기</Link>
                    </div>
                    <div>

                    </div>
                </div>
                <div className="Box2">
                    <div className=''>
                        <p>이달의 일정</p>
                        <Link to="/calendar" className="text-link">더보기</Link>
                    </div>
                    <div>
                        {/* 일정 리스트 표시 */}
                    </div>
                </div>
                <div className="Box3">
                    <div>
                        <p>공지사항</p>
                        <Link to="/Notice" className="text-link">더보기</Link>
                    </div>
                    <div>
                        {/* 공지사항 표시 */}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Main;