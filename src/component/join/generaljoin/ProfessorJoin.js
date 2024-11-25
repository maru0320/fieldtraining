import CollegeInput from "./CollegeInput";

function ProfessorJoin() {
    return (
        <div>
            <input className="inputbox" type="text" name="ID" placeholder="아이디를 입력해 주세요" />
            <br />
            <input className="inputbox" type="password" name="password" placeholder="비밀번호" />
            <br />
            <input className="inputbox" type="password" name="passwordCheck" placeholder="비밀번호 확인" />
            <br />
            <input className="inputbox" type="text" name="name" placeholder="이름" />
            <br />
            <CollegeInput />
            <input className="inputbox" type="email" name="email" placeholder="이메일주소" />
            <br />
            <input className="inputbox" type="tel" name="phoneNumber" placeholder="휴대전화번호" />
            <br />
            <input className="inputbox" type="tel" name="officeNumber" placeholder="사무실전화번호" />
        </div>
    );
}

export default ProfessorJoin;
