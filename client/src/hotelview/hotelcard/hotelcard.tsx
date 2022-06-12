import { KeycloakProfile } from 'keycloak-js';
import { useNavigate } from 'react-router';
import State from '../../state';
import { Hotel, Room } from '../hotelview';
import hotelli1 from '../../../public/static/hotelli_1.webp';
import hotelli2 from '../../../public/static/hotelli_2.webp';
import hotelli3 from '../../../public/static/hotelli_3.webp';
import hotelli4 from '../../../public/static/hotelli_4.webp';

const HotelCard = ({ id, img, location, name, email, phone }: Hotel) => {
  const navigate = useNavigate();
  // Books the first room that it finds.
  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const { hotel } = e.currentTarget.dataset;
    const selected_hotel = hotel ? (JSON.parse(hotel) as Hotel) : null;
    const rooms: Room[] | null = State.fetchStateByKey('rooms');

    // If rooms were found proceed...
    if (rooms) {
      const profile: KeycloakProfile | null= State.fetchStateByKey('profile');
      const foundRooms = rooms.filter((room) => room.hotel_id == selected_hotel?.id);

      profile == null ? alert('Sign in to do bookings') : null

      // If hotel and hotel.id was found proceed..
      // Ensure existence of profile.id, hotel, hotel.id and foundRooms has more than 0 items.
      if (selected_hotel && selected_hotel.id 
        && profile && profile.id 
        && foundRooms.length > 0) {
        (() => {
          navigate(`/results/rooms/${selected_hotel.id}`);
        })();
      }
    }
  }
  // Keeps a relative path reference to the hotel pictures.
  // This is kept to randomize how they are selected for the hotels.
  const hotelPictures = [
    hotelli1,
    hotelli2,
    hotelli3,
    hotelli4
  ];

  const hotel = { id, img, location, name, email, phone };
  return (
    <form
      className='results_main_frame'
      onSubmit={handleSubmit}
      key={id}
      data-hotel={JSON.stringify(hotel)}
    >
      <img src={hotelPictures[Math.floor(Math.random() * hotelPictures.length)]} className='results_main_frame_img' />
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
};

export default HotelCard;
