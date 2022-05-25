import * as React from 'react';
import './profile.css'
import Footer from "../homepage/footer/footer";
import ProfileHeader from './header';
import HotelCard from "../results/hotelcard/hotelcard";
import { Hotel } from "../results/results";
import State from "../state";

const Profile = () => {
  const [state, useState] = React.useState<Hotel[] | null>(null);
  if (State.fetchStateByKey('hotels') && !state) {
    useState(State.fetchStateByKey('hotels'));
  }
  return (
    <div className='profile_container'>
      <ProfileHeader />
      <div className='profile_sidebar'>
        <h3 style={{ color: 'white', justifySelf: 'center', fontWeight: 300 }}>Your Bookings:</h3>
        <div style={{ display: 'grid', overflow: 'scroll' }}>
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
      </div>
      <div className='profile_main'>
        <h3 style={{ color: 'white', justifySelf: 'center', fontWeight: 300 }}>Your Invoices:</h3>
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
      <div className='profile_footer'>
        <Footer />
      </div>
    </div>
  )
};

export default Profile;
