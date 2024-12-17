import React from "react";

const AddressSearch = ({ onAddressChange }) => {
  const handleComplete = (data) => {
    let addr = ""; // 주소 변수
    if (data.userSelectedType === "R") {
      addr = data.roadAddress; // 도로명 주소
    } else {
      addr = data.jibunAddress; // 지번 주소
    }

    const result = {
      postcode: data.zonecode,
      address: addr,
    };

    // 부모 컴포넌트에 전달
    onAddressChange(result);
  };

  const handleSearch = () => {
    new window.daum.Postcode({
      oncomplete: handleComplete,
    }).open();
  };

  return (
    <div>
      <button type="button" onClick={handleSearch}>
        우편번호 찾기
      </button>
    </div>
  );
};

export default AddressSearch;
