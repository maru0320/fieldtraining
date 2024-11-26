import React from "react";
import styled from "styled-components";

const CheckBox = ({ checkedItems, setCheckedItems, data }) => {
    const handleSingleCheck = (checked, id) => {
        setCheckedItems(prev =>
            checked ? [...prev, id] : prev.filter(item => item !== id)
        );
    };

    const handleAllCheck = (checked) => {
        setCheckedItems(checked ? data.map(item => item.id) : []);
    };

    const isAllChecked = checkedItems.length === data.length;

    return (
        <StyledTable>
            <thead>
                <tr>
                    <th>
                        <input
                            type="checkbox"
                            onChange={(e) => handleAllCheck(e.target.checked)}
                            checked={isAllChecked}
                        />
                    </th>
                    <th className="second-row">목록</th>
                </tr>
            </thead>
            <tbody>
                {data.map((item) => (
                    <tr key={item.id}>
                        <td>
                            <input
                                type="checkbox"
                                onChange={(e) => handleSingleCheck(e.target.checked, item.id)}
                                checked={checkedItems.includes(item.id)}
                            />
                        </td>
                        <td className="second-row">{item.title}</td>
                    </tr>
                ))}
            </tbody>
        </StyledTable>
    );
};

const StyledTable = styled.table`
  text-align: center;
  border-collapse: collapse;
  thead {
    tr {
      th {
        padding: 10px 15px;
        background-color: #888;
        color: #fff;
        font-weight: 700;
      }
    }
  }
  tbody {
    tr {
      td {
        padding: 7px 15px;
        border-bottom: 1px solid #eee;
      }
    }
  }
  .second-row {
    width: 150px;
  }
`;

export default CheckBox;
