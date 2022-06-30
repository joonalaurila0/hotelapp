import Footer from '../homepage/footer/footer';
import State from '../state';
import ResultHeader from './header/header';
import { Room } from './hotelview';
import { KeycloakProfile } from 'keycloak-js';
import huone1 from '../../public/static/hotelroom1.webp';
import huone2 from '../../public/static/hotelroom2.webp';
import huone3 from '../../public/static/hotelroom3.webp';
import huone4 from '../../public/static/hotelroom4.webp';
import huone5 from '../../public/static/hotelroom5.webp';
import huone6 from '../../public/static/hotelroom6.webp';
import huone7 from '../../public/static/hotelroom7.webp';
import huone8 from '../../public/static/hotelroom8.webp';
import huone9 from '../../public/static/hotelroom9.webp';
import huone10 from '../../public/static/hotelroom10.webp';
import huone11 from '../../public/static/hotelroom11.webp';
import huone12 from '../../public/static/hotelroom12.webp';
import Api, { BookingStatus } from '../api/api';
import { useParams } from 'react-router';
import { ISO8601Date } from '../util';
import { Link } from 'react-router-dom';
import Notify from '../homepage/notifier/notifier';
import React from 'react';

interface StateHook {
  called: boolean;
  msg: string;
}

/* View of the rooms */
const RoomView = () => {
  const rooms: Room[] | null = State.fetchStateByKey('rooms');
  const profile: KeycloakProfile = State.fetchStateByKey('profile');
  const { hotelid } = useParams(); // Get URL Param
  const [ notifyState, setNotifyState ] = React.useState<StateHook>({ called: false, msg: "" })

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const { room } = e.currentTarget.dataset;
    const selected_room = room ? (JSON.parse(room) as Room) : null;

    if (selected_room && profile && profile.id && hotelid) {
      (async (profileid) => {
        return (
          window.confirm('Are you sure you want to book this room?') &&
          /* Creates a booking for the user, which returns an invoice */
          Api.createBooking({
            customer_id: profileid,
            hotel_id: parseInt(hotelid),
            room_id: selected_room.id,
            booking_status: BookingStatus.Pending,
            start_date: new Date().toISOString(),
            end_date: ISO8601Date(7),
          }).then((res) =>
            res
              .json()
              .then((booking) => {
                /*
                 * NOTE: This evaluates whether there are more than one booking,
                 *        by evaluating whether it is an array, if it is,
                 *        set the new invoice into an array with the old ones.
                 *        Otherwise, the single invoice is pushed to state.
                 */
                try {
                  const bookings = State.fetchStateByKey('bookings');
                  Array.isArray(bookings)
                    ? State.storeStateToLocalStorage('bookings', [...bookings, booking])
                    : State.storeStateToLocalStorage('bookings', booking);
                } catch (e) {
                  console.error(e);
                }
              })
              .then(() => {
                setNotifyState({ called: true, msg: "Booking succesfully made!" });
                setTimeout(() => setNotifyState({ msg: "", called: false },), 8000) // cleanup
              })
          )
        );
      })(profile.id);
    }
  };

  // Keeps a relative path reference to the room pictures.
  // This is kept to randomize how they are selected for the rooms.
  const roomPictures = [
    huone1,
    huone2,
    huone3,
    huone4,
    huone5,
    huone6,
    huone7,
    huone8,
    huone9,
    huone10,
    huone11,
    huone12,
  ];
  return (
    <div className='results_container'>
      {notifyState.called 
        ? (<Notify called={notifyState.called} message={notifyState.msg} />) 
        : (null)}
      <ResultHeader results={rooms ? rooms.length : 0} description='Rooms' />
      <div className='results_sidebar'>
        <h1 className='logo' style={{ justifyContent: 'center', fontWeight: 300 }}>
          Hotely
        </h1>
        <div>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300 }}>
            Results found: {rooms && rooms.length}
          </h4>
          <h4
            style={{
              color: 'white',
              fontSize: '1.6rem',
              fontWeight: 300,
              textDecorationLine: 'underline',
            }}
          >
            <Link to={'/'}>Back to homepage</Link>
          </h4>
          <h4
            style={{
              color: 'white',
              fontSize: '1.6rem',
              fontWeight: 300,
              textDecorationLine: 'underline',
            }}
          >
            <Link to={'/results'}>Back to hotels</Link>
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
        {rooms?.map((room: Room) => {
          return (
            <form
              className='results_main_frame'
              onSubmit={onSubmit}
              key={room.id}
              data-room={JSON.stringify(room)}
            >
              <img
                src={roomPictures[Math.floor(Math.random() * roomPictures.length)]}
                className='results_main_frame_img'
              />
              <div className='results_main_frame_info'>
                <h4>Room number: {room.id}</h4>
                <p>Room price: ${room.booking_price}</p>
                <p>Room capacity: {room.capacity} people</p>
                <p>
                  Room size: {room.room_area}m<sup>2</sup>
                </p>
              </div>
              <div className='results_main_frame_last'>
                <button disabled={notifyState.called ? true : false}>Book now</button>
              </div>
            </form>
          );
        })}
      </div>
      <div className='results_footer'>
        <Footer />
      </div>
    </div>
  );
};

export default RoomView;
