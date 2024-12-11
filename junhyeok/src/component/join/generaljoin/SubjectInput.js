import React from 'react';

function SubjectInput({ value, onChange }) {
    return (
        <div>
            <select
                className="subjectChoice"
                id="subject"
                name="subject"
                value={value} // 부모로부터 받은 값을 설정
                onChange={onChange} // 부모에서 전달된 onChange 함수 호출
            >
                <option value="" disabled>교과목을 선택하세요.</option>
                <option value="korean">국어</option>
                <option value="english">영어</option>
                <option value="math">수학</option>
            </select>
        </div>
    );
}

export default SubjectInput;