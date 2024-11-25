import './Login.css';
import { useNavigate } from "react-router-dom";


function Login() {

    const navigate = useNavigate();

    const onClickFindID = () => {
        navigate("/findid");
    };

    const onClickFindPassword = () => {
        navigate("/findpassword");
    };

    return (
        <div className="loginWrapper">

        
            <h1>로그인</h1>
            <input className="inputbox" type="text" name="ID" placeholder="아이디" />

            <input className="inputbox" type="password" name="password" placeholder="비밀번호" />
            <br></br>
            <button type="submit" >로그인</button>
            <br></br>

            <div className="loginButtonContainer">
                <button className="loginButton" onClick={onClickFindID}>아이디찾기</button>
                <button className="loginButton" onClick={onClickFindPassword}>비밀번호찾기</button>
            </div>
           
        </div>
        
    );
}
export default Login;