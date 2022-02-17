import * as React from 'react';
import './main.css';
import Header from '../header/header';
import Search from '../../search/search.component';

const Main = () => {
  const [scrollDir, setScrollDir] = React.useState(false);
  return (
    <div className='main'>
      <div className='squaresfirst'>
        <Header />
      </div>
      <div className='squares'>
        <div>
          <h1 style={{ width: '100%' }}>
            Hotely finds you the hotel you want
          </h1>
        </div>
      </div>
      <div className='squares2' style={{ justifyContent: 'center' }}>
        <div className='innerSquare2'>
          <Search scrollEvent={scrollDir} />
        </div>
        <a href="" style={{ justifySelf: 'center', fontSize: '1.2rem', padding: '1.1rem 2.5rem' }}>Search for hotels</a>
      </div>
      <div className='squares3'>
        <div className='contact__text'></div>
      </div>
      <div className='imageColumn'>
        <div className='imageColumn_LANG_SELECT'>
          <p>Suomeksi <div id='finflag' /></p>
        <p>English <div id='enflag' /></p>
        </div>
      </div>
    </div>
  )
}

export default Main;
