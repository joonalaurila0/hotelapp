import { KeycloakProfile } from 'keycloak-js';
import { useNavigate } from 'react-router';
import kuva from '../../../public/static/placeholder.jpg';
import Api, { Booking, BookingStatus, Invoice } from '../../api/api';
import State from '../../state';
import { ISO8601Date, TimestampNow } from '../../util';
import { Hotel, Room } from '../hotelview';

const HotelCard = ({ id, img, location, name, email, phone }: Hotel) => {

  const navigate = useNavigate();
  // Books the first room that it finds.
  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const { hotel } = e.currentTarget.dataset;
    const selectedHotel = hotel ? JSON.parse(hotel) as Hotel : null;
    const rooms: Room[] | null = State.fetchStateByKey('rooms');

    // If rooms were found proceed...
    if (rooms) {
      const profile: KeycloakProfile = State.fetchStateByKey('profile');
      const foundRooms = rooms.filter((room) => room.hotel_id == selectedHotel?.id);
      console.log("Found rooms :: ", foundRooms)

      // If hotel and hotel.id was found proceed..
      // Ensure existence of profile.id, hotel, hotel.id and foundRooms has more than 0 items.
      if (selectedHotel && selectedHotel.id 
        && profile.id && foundRooms.length > 0) {
        const args = { 
          profileId: profile.id,
          hotel: selectedHotel,
          roomId: foundRooms[0].id,
          roomPrice: foundRooms[0].booking_price
        };
        (() => {
          console.log("handleSubmit ::", selectedHotel)
          navigate(`/results/rooms/${selectedHotel.id}`)
        })()
        //(() => {
        //  return window.confirm("Are you sure you want to book hotel?") 
        //    && true;
        //})() && Api.createBookingAndInvoice(args);
      }
    } 
  }

  const hotel = { id, img, location, name, email, phone };
  return (
    <form 
      className='results_main_frame' 
      onSubmit={handleSubmit}
      key={id}
      data-hotel={JSON.stringify(hotel)}
    >
      <img src={kuva} className='results_main_frame_img' />
      <div className='results_main_frame_info'>
        <h4>{name}</h4>
        <p>Location: {location}</p>
        <p>Email: {email}</p>
        <p>Phone: {phone}</p>
      </div>
      <div className='results_main_frame_last'>
        <button>Look for rooms</button>
      </div>
    </form>
  );
}

export default HotelCard;
