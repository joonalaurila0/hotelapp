import './footer.css'
import { SiFacebook } from 'react-icons/si';
import { SiInstagram } from 'react-icons/si';
import { SiYoutube } from 'react-icons/si';
import { SiTwitter } from 'react-icons/si';


const Footer = () => {
  return (
    <footer className='footer'>
      <div className='row'>
        <a href='#'>
          <i className='fa fa-twitter'>
            <SiTwitter />
          </i>
        </a>
        <a href='#'>
          <i className='fa fa-youtube'>
            <SiYoutube />
          </i>
        </a>
        <a href='#'>
          <i className='fa fa-instagram'>
            <SiInstagram />
          </i>
        </a>
        <a href='#'>
          <i className='fa fa-facebook'>
            <SiFacebook />
          </i>
        </a>
      </div>
      <div className='row'><p className='rights'>All rights reserved</p></div>
      <div className='row'>
        <ul>
          <li>
            <a href='#'>Contact</a>
          </li>
          <li>
            <a href='#'>About me</a>
          </li>
          <li>
            <a href='#'>Privacy Policy</a>
          </li>
          <li>
            <a href='#'>Terms & Conditions</a>
          </li>
        </ul>
      </div>
    </footer>
  )
}

export default Footer;
