import React from 'react';
import { Link } from 'react-router-dom';
import './Menu.css';


function Menu({ menu_list }) {
  return (
    <nav>
      <ul>
        {menu_list.map((menu) => (
          <li key={menu.menu_id}>
            <Link to={menu.url}>{menu.title}</Link>
            {menu.subItems.length > 0 && (
              <ul>
                {menu.subItems.map((subItem) => (
                  <li key={subItem.sub_id}>
                    <Link to={subItem.url}>{subItem.title}</Link>
                  </li>
                ))}
              </ul>
            )}
          </li>
        ))}
      </ul>
    </nav>
  );
}

export default Menu;
