import axios from "axios";
import Pagination from "../../Pagination";
import { useState } from "react";

const DepartmentSearch = () => {
    const [searchKeyword, setSearchKeyword] = useState(""); //검색어
    const [results, setResults] = useState([]); // 검색 결과 상태
    const postsPerPage = 10; // 한 페이지에 보여줄 항목 수

    const handleInputChange = (e) => {
        setSearchKeyword(e.target.value);
    };

    const handleInputSearch = async () => {
        try{
            const response = await axios.get('http://localhost:8090/api/listmanagement/search', {
                params: { department: searchKeyword }
            });
            setResults(response.data);
        } catch (error) {
            console.log("검색 중 오류 발생" + error);
            alert("오류! 다시 검색해주세요");
        }
    }

    return (
        <div className="ListManagement">
            <div className="section">
                <h2 className="title">명단관리</h2>
            </div>
            <div className="section">
                {/*검색 창 및 버튼 클릭시 스프링부트로 텍스트 전송*/}
                <input 
                    type="text"
                    placeholder="학과 검색"
                    value={searchKeyword}
                    onChange={handleInputChange}
                />
                <button onClick={handleInputSearch}>검색</button>
            </div>
            <div className="section">
                {/*실습생 명단 나열*/}
                {results.length > 0 ? (
                    results.map((result, index) => (
                        <div key={index}>
                            <p>이름: {result.name}</p>
                            <p>학과: {result.department}</p>
                            <p>이메일: {result.email}</p>
                        </div>
                        ))
                    ) : (
                    <p>검색 결과가 없습니다.</p>
                )}
            </div>
            <div className="section Pagination">
                
            </div>
        </div>
    );
};

export default DepartmentSearch;