import kuva from '../../../public/static/placeholder.jpg';
import { Hotel } from '../results';

const HotelCard = ({ id, img, location, name, email, phone }: Hotel) => {
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const { hotel } = e.currentTarget.dataset;
    let selectedHotel = hotel ? JSON.parse(hotel) as Hotel : null;
    window.confirm("Are you sure you want to book hotel?") && window.alert(`Hotel ${selectedHotel?.name} booked!`);

    // Make a call to the API and/or to the event bus w/e about the booking
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
        <p>{location}</p>
        <p>{email}</p>
        <p>{phone}</p>
      </div>
      <div className='results_main_frame_last'>
        <p>Hotel price: $40</p>
        <button>Book now</button>
      </div>
    </form>
  );
}

export default HotelCard;
