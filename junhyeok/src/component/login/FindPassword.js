function FindPassword() {
    return (
        <div className="loginWrapper">

        
            <h1>비밀번호 찾기</h1>
           
            <input className="inputbox" type="text" name="ID" placeholder="아이디" />
            <input className="inputbox" type="email" name="email" placeholder="이메일주소" />
            <br></br>
            <button>다음</button>
        </div>
        
    );
}
export default FindPassword;