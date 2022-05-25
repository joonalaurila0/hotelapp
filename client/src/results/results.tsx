import * as React from 'react';
import './results.css'
import ResultHeader from './header/header'
import HotelCard from './hotelcard/hotelcard';
import State from '../state';
import Footer from '../homepage/footer/footer';

export interface Hotel {
  id: string;
  img: string;
  location: string;
  name: string;
  email: string;
  phone: string;
}

const Results = () => {
  const [state, useState] = React.useState<Hotel[] | null>(null);
  if (State.fetchStateByKey('hotels') && !state) {
    useState(State.fetchStateByKey('hotels'));
  }
  return (
    <div className='results_container'>
      <ResultHeader results={state ? state.length : 0} />
      <div className='results_sidebar'>
        <h1 className='logo' style={{ justifyContent: 'center', fontWeight: 300 }}>Hotely</h1>
        <div>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300 }}>Results found: {state?.length}</h4>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300, textDecorationLine: 'underline' }}><a href='/'>Back to homepage</a></h4>
          <h4 style={{ color: 'beige', fontSize: '1.8rem', fontWeight: 300, textDecorationLine: 'underline', fontFamily: 'smooch' }}><a href='/profile'>My Profile</a></h4>
        </div>
      </div>
      <div className='results_main'>
        {state && state.map((hotel: Hotel) => {
          return (<HotelCard 
            key={hotel.id}
            id={hotel.id} 
            img={hotel.img} 
            location={hotel.location}
            name={hotel.name}
            email={hotel.email}
            phone={hotel.phone}
          />)
        })}
      </div>
      <div className='results_footer'>
        <Footer />
      </div>
    </div>
  )
}

export default Results;
