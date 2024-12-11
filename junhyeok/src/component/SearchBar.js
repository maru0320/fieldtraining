import React, { useState } from 'react';

const SearchBar = ({onSearch}) => {
    const [searchTerm, setSearchTerm] = useState("");

    // 검색어 상태 변경
    const handleChange = (event) => {
        setSearchTerm(event.target.value);
    };

    // 검색 버튼 클릭 시 부모에게 검색어 전달
    const handleSearch = () => {
        onSearch(searchTerm);
    };

    return (
        <div className="search-bar">
            <input
                type="text"
                placeholder="검색..."
                value={searchTerm}
                onChange={handleChange}
            />
            <button onClick={handleSearch}>검색</button>
        </div>
    );
};

export default SearchBar;