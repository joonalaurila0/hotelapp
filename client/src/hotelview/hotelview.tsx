import './hotelview.css';
import ResultHeader from './header/header';
import HotelCard from './hotelcard/hotelcard';
import State from '../state';
import Footer from '../homepage/footer/footer';
import { KeycloakProfile } from 'keycloak-js';
import { Link } from 'react-router-dom';

export type Hotel = {
  id: number;
  img: string;
  location: string;
  name: string;
  email: string;
  phone: string;
};

enum RoomStatus {
  Available,
  Reserved,
  Occupied,
  NotAvailable,
  BeingServiced,
  Other,
}
export type Room = {
  id: number;
  hotel_id: number;
  availability: boolean;
  booking_price: number;
  capacity: number;
  room_area: number;
  room_status: RoomStatus;
};

/* View of the hotels */
const HotelView = () => {
  const hotels: Hotel[] | null = State.fetchStateByKey('hotels');
  const profile: KeycloakProfile = State.fetchStateByKey('profile');
  return (
    <div className='results_container'>
      <ResultHeader results={hotels ? hotels.length : 0} description='Hotels' />
      <div className='results_sidebar'>
        <h1 className='logo' style={{ justifyContent: 'center', fontWeight: 300 }}>
          Hotely
        </h1>
        <div>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300 }}>
            Results found: {hotels && hotels.length}
          </h4>
          <h4
            style={{
              color: 'white',
              fontSize: '1.6rem',
              fontWeight: 300,
              textDecorationLine: 'underline',
            }}
          >
            <a href='/'>Back to homepage</a>
          </h4>
          <h4
            style={{
              color: 'beige',
              fontSize: '1.8rem',
              fontWeight: 300,
              textDecorationLine: 'underline',
              fontFamily: 'smooch',
            }}
          >
            {profile && <Link to={'/profile'}>My Profile</Link>}
          </h4>
        </div>
      </div>
      <div className='results_main'>
        {hotels?.map((hotel: Hotel) => {
          return (
            <HotelCard
              key={hotel.id}
              id={hotel.id}
              img={hotel.img}
              location={hotel.location}
              name={hotel.name}
              email={hotel.email}
              phone={hotel.phone}
            />
          );
        })}
      </div>
      <div className='results_footer'>
        <Footer />
      </div>
    </div>
  );
};

export default HotelView;
