import React from 'react';
import "./SubjectInput.css";

function SubjectInput({ value, onChange, style }) {
    return (
        <div>
            <select
                className="subjectChoice"
                id="subject"
                name="subject"
                value={value} // 부모로부터 받은 값을 설정
                onChange={onChange} // 부모에서 전달된 onChange 함수 호출
                style={style} // 부모에서 전달된 스타일을 직접 적용
            >
                <option value="" disabled>교과목을 선택하세요.</option>
                <option value="국어">국어</option>
                <option value="영어">영어</option>
                <option value="수학">수학</option>
            </select>
        </div>
    );
}

export default SubjectInput;
