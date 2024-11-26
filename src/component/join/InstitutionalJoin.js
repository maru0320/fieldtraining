import './InstitutionalJoin.css';
import UploadBox from '../UploadBox';


function InstitutionalJoin() {

    
  



    return (
        <form>

            <h2>기관 회원가입</h2>
            <div className="wrapper">

            <input className="inputbox" type="text" name="ID" placeholder="아이디를 입력해 주세요" />
       
            <input className="inputbox" type="password" name="password" placeholder="비밀번호" />

            <input className="inputbox" type="password" name="passwordCheck" placeholder="비밀번호 확인" />
          
            <input className="inputbox" type="tel" name="mainNumber" placeholder="대표전화번호" />

            <input className="inputbox" type="text" name="address" placeholder="주소" />
            
            
            <UploadBox />
            

            <input className="inputbox" type="text" name="manager_ID" placeholder="담당자ID" />


                <p>
                <button className="joinButton" type="submit">가입하기</button>
                </p>

            </div>
        </form>

        
    );
}
export default InstitutionalJoin;