import { useState } from "react";
import './GeneralJoin.css';
import StudentJoin from "./generaljoin/StudentJoin";
import TeacherJoin from "./generaljoin/TeacherJoin";
import ProfessorJoin from "./generaljoin/ProfessorJoin";


function GeneralJoin() {

    const [value, setValue] = useState('');

    const handleChange = (e) => {
        setValue(e.target.value);
    };


    function partChoice(){
        if ( value === 'student'){
            return(
                <StudentJoin />
            );
        } else if( value === 'teacher'){
            return (
                <TeacherJoin />
            );
        } else if( value === 'professor'){
            return (
                <ProfessorJoin />
            )
        }
    }

    

    return (
        <form>

            
            <div className="wrapper">
            <h2>일반 회원가입</h2>
                <div class="part_wrapper">
                    <div class="part">
                        <input type="radio" name="part" value="student" onChange={handleChange} /> 교육실습생
                    </div>
                    <div class="part">
                        <input type="radio" name="part" value="teacher" onChange={handleChange} /> 실습협력학교
                    </div>
                    <div class="part">
                        <input type="radio" name="part" value="professor" onChange={handleChange} /> 교육양성기관
                    </div>
                </div>
                

                <div>
                    {partChoice()}
                </div>

              
             
                <p>
                    <button className="joinButton" type="submit">가입하기</button>
                </p>

            </div>
        </form>

        
    );
}
export default GeneralJoin;