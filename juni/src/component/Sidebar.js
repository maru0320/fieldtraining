// Sidebar.js

import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';

function Sidebar({ menu_list }) {
  const [openMenus, setOpenMenus] = useState({});

  const toggleMenu = (menu_id) => {
    setOpenMenus((prev) => ({
      ...prev,
      [menu_id]: !prev[menu_id],
    }));
  };

  return (
    <div className="sidebar">
      {menu_list && menu_list.map((menu) => (
        <div key={menu.menu_id}>
          <button onClick={() => toggleMenu(menu.menu_id)} className="menuToggleBtn">
            {menu.title} {openMenus[menu.menu_id] ? "▲" : "▼"}
          </button>
          {openMenus[menu.menu_id] && menu.subItems && (
            <div className="subMenuList">
              {menu.subItems.map((subItem) => (
                <Link key={subItem.sub_id} to={subItem.url} className="subMenuItem">
                  {subItem.title}
                </Link>
              ))}
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default Sidebar;
