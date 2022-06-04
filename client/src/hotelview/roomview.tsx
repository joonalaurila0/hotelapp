import Footer from "../homepage/footer/footer";
import State from "../state";
import ResultHeader from "./header/header";
import HotelCard from "./hotelcard/hotelcard";
import { Room } from "./hotelview";
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

/* View of the rooms */
const RoomView = () => {
  const rooms: Room[] | null = State.fetchStateByKey('rooms');

  const onSubmit = () => {};

  // Keeps a relative path reference to the room pictures.
  // This is kept to randomize how they are selected for the rooms.
  const roomPictures = [
    huone1, huone2, huone3, 
    huone4, huone5, huone6,
    huone7, huone8, huone9,
    huone10, huone11, huone12
  ];
  return (
    <div className='results_container'>
      <ResultHeader results={rooms ? rooms.length : 0} description='Rooms' />
      <div className='results_sidebar'>
        <h1 className='logo' style={{ justifyContent: 'center', fontWeight: 300 }}>Hotely</h1>
        <div>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300 }}>Results found: {rooms && rooms.length}</h4>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300, textDecorationLine: 'underline' }}><a href='/'>Back to homepage</a></h4>
          <h4 style={{ color: 'white', fontSize: '1.6rem', fontWeight: 300, textDecorationLine: 'underline' }}><a href='/results'>Back to hotels</a></h4>
          <h4 style={{ color: 'beige', fontSize: '1.8rem', fontWeight: 300, textDecorationLine: 'underline', fontFamily: 'smooch' }}><a href='/profile'>My Profile</a></h4>
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
      <img src={roomPictures[Math.floor(Math.random() * roomPictures.length)]} className='results_main_frame_img' />
      <div className='results_main_frame_info'>
        <h4>Room number: {room.id}</h4>
        <p>Room price: {room.booking_price}</p>
        <p>Room capacity: {room.capacity}</p>
        <p>Room size: {room.room_area}</p>
      </div>
      <div className='results_main_frame_last'>
        <button>Book now</button>
      </div>
    </form>

          )
        })}
      </div>
      <div className='results_footer'>
        <Footer />
      </div>
    </div>
  )
}

export default RoomView;
