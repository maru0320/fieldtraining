import axiosInstance from "../axiosInstance";
import React, { useState } from "react";

function FindID() {

    const [formData, setFormData] = useState({
        name : '',
        email : ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

const findUserId = async () => {
    try {
        const response = await axiosInstance.post("/find/Id", {
            name: formData.name,
            email: formData.email
          });
      
        if (response.status == 200){
            alert(`아이디는: ${response.data}`);
        }

    } catch(error){
        console.error("실패", error);
        alert(error.response?.data || "아이디를 찾을 수 없습니다.");
    }
};
const handleSubmit = (e) => {
    e.preventDefault();
    findUserId();
  };

    return (
        <div className="loginWrapper">
        <h1>아이디 찾기</h1>
        <form onSubmit={handleSubmit}>
          <input 
            className="inputbox" 
            type="text" 
            name="name" 
            value={formData.name} 
            placeholder="이름" 
            onChange={handleChange}
          />
          <input 
            className="inputbox" 
            type="email" 
            name="email" 
            value={formData.email} 
            placeholder="이메일주소" 
            onChange={handleChange}
          />
          <br />
          <button type="submit">아이디찾기</button>
        </form>
      </div>
        
    );
}
export default FindID;