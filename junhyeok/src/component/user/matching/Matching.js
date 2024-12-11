import React, { useState } from "react";
import Select from "react-select";
import CheckBox from "./CheckBox";
import Arrow from "../../../img/Arrow.png";
import "./Matching.css";

function Matching() {
    const options = [
        { value: 'korean', label: '국어' },
        { value: 'math', label: '수학' },
        { value: 'english', label: '영어' },
    ];

    const data = [
        { id: 0, title: '김마루' },
        { id: 1, title: '박준' },
        { id: 2, title: '최지원' },
        { id: 3, title: '김상훈' },
        { id: 4, title: '장경수' },
    ];

    const data2 = [
        { id: 5, title: '김선생' },
        { id: 6, title: '박선생' },
        { id: 7, title: '최선생' },
        { id: 8, title: '김선생' },
        { id: 9, title: '장선생' },
    ];

    const data3 = [
        { id: 10, title: '김쌤' },
        { id: 11, title: '박쌤' },
        { id: 12, title: '최쌤' },
        { id: 13, title: '김쌤' },
        { id: 14, title: '장쌤' },
    ];

    const [teacher, setTeacher] = useState({
        Name : '김마루',
        School : '도마초등학교',
        Subject : '수학',
        Tel : '010-7878-7897',
        Email : 'maru7700@daum.net'
  });

  const [student, setStudent] = useState({
        Name : '박준',
        College : '배재대학교',
        Department : '수학교육과',
        Subject : '수학',
        Student_code : '1961017',
        Tel : '010-7553-4719',
        Email : 'juni730@daum.net'
  });

    const placeholder = '교과목을 선택하세요.';
    const [selected, setSelected] = useState('');
    const [checkedItems, setCheckedItems] = useState([]);

    const onChangeSelect = (e) => {
        if (e) setSelected(e.value);
        else setSelected('');
    };

    const onClickCheck = () => {
        if (checkedItems.length === 0) {
            alert("체크박스를 선택해주세요.");
            return;
        }
        alert("신청되었습니다.");
    };

    // 선택된 과목에 따라 동적으로 데이터 변경
    let displayData;
    if (selected === 'korean') {
        displayData = data;
    } else if (selected === 'math') {
        displayData = data2;
    } else if (selected === 'english') {
        displayData = data3;
    }


    // 매칭 전후 확인 버튼
    let testButton = true;
    const onClickButton = () => {
        
    }
    

    return (
        
        <div className="container">
            <h1>매칭현황</h1>
            <button>매칭 전후 버튼</button>
            {/*매칭 전*/}
            <div>
                <Select
                    onChange={onChangeSelect}
                    options={options}
                    placeholder={placeholder}
                    isSearchable={false}
                />
                {/* 조건에 맞는 체크박스 표시 */}
                {selected && (
                    <CheckBox
                        checkedItems={checkedItems}
                        setCheckedItems={setCheckedItems}
                        data={displayData}  // 동적으로 변경된 data 전달
                    />
                )}
                <button onClick={onClickCheck}>신청</button>
            </div>

            {/*매칭 후*/}
            <div className="after_info">
                    <table className="left_info">
                        <tr>
                            <h2>{student.Name}</h2>   
                        </tr>
                        <tr>
                            <p>{student.College}</p>   
                            <p>{student.Department}</p> 
                            <p>{student.Subject}</p>
                            <p>{student.Student_code}</p>
                            <p>{student.Tel}</p>
                            <p>{student.Email}</p>
                        </tr>
                    </table>
                    <img src={Arrow} width = '100px'/>
                    <table className="right_info">
                        <tr>
                            <h2>{teacher.Name}</h2>
                        </tr>
                        <tr>
                            <p>{teacher.School}</p>   
                            <p>{teacher.Subject}</p> 
                            <p>{teacher.Email}</p>
                            <p>{teacher.Tel}</p>
                        </tr>
                    </table>
                </div>
        </div>
    );
}

export default Matching;
