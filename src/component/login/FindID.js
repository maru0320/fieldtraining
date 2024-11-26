function FindID() {
    return (
        <div className="loginWrapper">

        
            <h1>아이디 찾기</h1>
            <input className="inputbox" type="text" name="name" placeholder="이름" />
            <input className="inputbox" type="email" name="email" placeholder="이메일주소" />
            <br></br>
            <button type="submit">다음</button>
        </div>
        
    );
}
export default FindID;