import React from 'react';
import './Footer.css';

function Footer() {
    return (
        <footer className="footer">
            <div className="footer-content">
                <p>
                <span>회사명: 한국교육개발원</span>
                <span>  /  주소: 대전광역시 @@구 @@동 123-45</span>
                <span>  /  전화번호: 042-123-5678</span>
                <span>  /  이메일: example@company.com</span>
                </p>
            </div>
        </footer>
    );
}

export default Footer;