import './main.css';
import Header from '../header/header';
import Search from '../../search/search';
import { keycloak } from '../../keycloak';
const Main = () => {
  console.log('Token parsed ::', keycloak.idTokenParsed);
  return (
    <div className='main'>
      <div className='squaresfirst'>
        <Header />
      </div>
      <div className='squares'>
        <div>
          <h1 style={{ width: '100%' }}>Hotely finds you the hotel you want</h1>
        </div>
      </div>
      <div className='squares2' style={{ justifyContent: 'center' }}>
        <div className='innerSquare2'>
          <Search scrollEvent={true} />
        </div>
      </div>
      <div className='squares3'>
        <div className='contact__text'></div>
      </div>
      <div className='imageColumn'>
        <div className='imageColumn_WRAPPER'>
          <div className='imageColumn_AUTH'>
            <a style={{ cursor: 'pointer' }} onClick={() => keycloak.login()} title='Sign in'>
              Sign in
            </a>
          </div>
          <div className='imageColumn_LANG_SELECT'>
            <div className='langbox' id='finflag'>
              <p onClick={() => console.log(keycloak.tokenParsed)}>Suomeksi </p>
            </div>
            <div className='langbox' id='enflag'>
              <p>English</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Main;
