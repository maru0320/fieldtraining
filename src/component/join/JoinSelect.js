import './JoinSelect.css';
import { useNavigate } from "react-router-dom";


function JoinSelect() {

    const navigate = useNavigate();

    const onClickGeneralJoin = () => {
        navigate("/generaljoin");
    };

    const onClickInstitutionalJoin = () => {
        navigate("/institutionaljoin");
    };

    return (

        <div>

            <h1>가입하실 회원 유형을 선택해주세요</h1>

          
                    <button className="partButton" onClick={onClickGeneralJoin}>일반 회원가입
                        <p>교육실습생, 실습협력학교(담당교사),교원양성기관(교수)</p>
                    </button>

                

                    <button className="partButton" onClick={onClickInstitutionalJoin}>기관 회원가입
                        <p>실습협력학교 관리자, 교원양성기관 관리자 </p>
                    </button>

                
         
            

        </div>
    );
}
export default JoinSelect;