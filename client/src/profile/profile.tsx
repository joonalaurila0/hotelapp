import * as React from 'react';
import './profile.css'
import Footer from "../homepage/footer/footer";
import ProfileHeader from './header';
import HotelCard from "../hotelview/hotelcard/hotelcard";
import { Hotel } from "../hotelview/hotelview";
import State from "../state";
import Api, { Booking, Invoice } from '../api/api';
import { KeycloakProfile } from 'keycloak-js';
import { keycloak } from '../keycloak';

const Profile = () => {
  const [state, setState] = React.useState<Hotel[] | null>();
  //if (State.fetchStateByKey('hotels') && !state) {
  //  setState(State.fetchStateByKey('hotels'));
  //}

  console.log("State profile.tsx ::", state)
  const profile: KeycloakProfile | null = State.fetchStateByKey('profile');

  if (profile && profile.id) {
    (async (profileId: string) => {

      const res: Response = 
        (await Api.findInvoices(profileId))
      const invoices: Array<Invoice> = await res.json();
      invoices ? State.storeStateToLocalStorage('invoices', invoices) : null;

      const res2: Response =
        (await Api.findBookings(profileId))
      const bookings: Array<Booking> = await res2.json();
      bookings ? State.storeStateToLocalStorage('bookings', bookings) : null;

    })(profile.id)

    //Api.findBookings(profile.id).then((res) => {
    //  res.json().then((json) => {
    //    State.storeStateToLocalStorage(`my-bookings`, json)
    //  })
    //})
    //Api.findInvoices(profile.id).then((res) => {
    //  res.json().then((json) => {
    //    State.storeStateToLocalStorage(`my-invoices`, json)
    //  })
    //})
  }

  const invoices: Array<Invoice> | null = State.fetchStateByKey('invoices');
  const bookings: Array<Booking> | null = State.fetchStateByKey('bookings');
  const hotels: Array<Hotel> | null = State.fetchStateByKey('hotels');

  console.log("State profile.tsx ::", state)
  console.log("Invoices profile.tsx ::", invoices)

  return (
    <div className='profile_container'>
      <ProfileHeader />
      <div className='profile_sidebar'>
        <h3 style={{ color: 'white', justifySelf: 'center', fontWeight: 300 }}>Your Bookings:</h3>
        <div style={{ display: 'grid', overflow: 'scroll' }}>
        {bookings && bookings.map((booking: Booking) => {
          return (<div 
            key={booking.id} 
            style={{
              display: 'flex', flexDirection: 'column',
              fontSize: '1em', color: 'white',
              borderTop: '1px solid #a67b5b',
              borderBottom: '1px solid #a67b5b',
              paddingLeft: '.5em'
            }}>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Booking ID: {booking.id}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Hotel ID: {booking.hotel_id}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Room booked: {booking.room_id}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Booking status: {booking.booking_status}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Booking start-end dates: {booking.start_date + " - " + booking.end_date}</p>
          </div>)})}
        </div>
      </div>
      <div className='profile_main'>
        <h3 style={{ color: 'white', justifySelf: 'center', fontWeight: 300 }}>Your Invoices:</h3>
        {invoices && invoices.map((invoice: Invoice) => {
          return (<div 
            key={invoice.id} 
            style={{
              display: 'flex', flexDirection: 'column',
              fontSize: '1em', color: 'white',
              borderTop: '1px solid #a67b5b',
              borderBottom: '1px solid #a67b5b',
              paddingLeft: '.5em'
            }}>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice ID: {invoice.id}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Issued: {invoice.issued}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice total: ${invoice.total}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice paid: {invoice.paid ? "Paid" : "Not paid"}</p>
            <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice payment date: {invoice.payment_date}</p>
          </div>)
        })}
      </div>
      <div className='profile_footer'>
        <Footer />
      </div>
    </div>
  )
};

export default Profile;
