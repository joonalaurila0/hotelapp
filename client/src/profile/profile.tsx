import './profile.css';
import Footer from '../homepage/footer/footer';
import ProfileHeader from './header';
import State from '../state';
import Api, { Booking, Invoice } from '../api/api';
import { KeycloakProfile } from 'keycloak-js';
import { evalArray, evalObject } from '../util';

const Profile = () => {
  const profile: KeycloakProfile | null = State.fetchStateByKey('profile');

  /**
   * Performs two requests to services for client bookings and invoices
   * and overwrites the state with the request data conditionally.
   * @param profileId - Identifier of the profile.
   */
  const fetchProfileData = async (profileId: string): Promise<void> => {
    const res: Response = await Api.findInvoices(profileId);
    const invoices: Array<Invoice> = await res.json();
    invoices ? State.storeStateToLocalStorage('invoices', invoices) : null;

    const res2: Response = await Api.findBookings(profileId);
    const bookings: Array<Booking> = await res2.json();
    bookings ? State.storeStateToLocalStorage('bookings', bookings) : null;
  }

  // Replace this with this instead:
  //
  // First checks from History API if this is the user's first time
  // visiting this page, IF IT IS, run all the requests, IF IT IS NOT
  // then do not run the requests.
  //
  // This could also have some kind of cooldown to clear the state 
  // to make sure it does not get stale all the time.
  if (
    profile &&
    profile.id
  ) {
    // NOTE: This function checks for the time difference 
    // to be greater than or equal to 5 minutes for the state refresh/fetch.
    ((profileId) => {
      let lastVisit: string | null = window.localStorage.getItem('profile_lastVisited');
      if (lastVisit == null) {
        // Set time of last visiting this page.
        window.localStorage.setItem('profile_lastVisited', JSON.stringify(Date.now()));
        fetchProfileData(profileId);
        return;
      }

      if (lastVisit != null) {
        // Get the time difference of now and last visit in seconds.
        const timeDifference = ((Date.now() - JSON.parse(lastVisit)) / 1000);
        console.debug("Current time difference in seconds: ", timeDifference);
        timeDifference >= 300 
          ? fetchProfileData(profileId) // Time difference is equal to or greater than 5 minutes, perform state refresh for the profile page.
          : null // Time difference is less than 5 minutes, then do nothing.
      }
    })(profile.id);
  }

  const invoices: Array<Invoice> | Invoice | null = State.fetchStateByKey('invoices');
  const bookings: Array<Booking> | Booking | null = State.fetchStateByKey('bookings');

  // The evalArray and evalObject is used to evaluate type information to be
  // able to set the invoices and bookings to the profile page. This is evaluated
  // by a chained conditional to choose between Array, Object or null.
  // The actual data is fetched from the localStorage for persistence and fetched from the APIs.
  //
  // The second conditional that uses evalObject to evaluate if object is not an array nor null,
  // is transformed into an array using Array.of, this way Object can still be set with map.
  // Spec: https://tc39.es/ecma262/multipage/indexed-collections.html#sec-array.of
  return (
    <div className='profile_container'>
      <ProfileHeader />
      <div className='profile_sidebar'>
        <h3 style={{ color: 'white', justifySelf: 'center', fontWeight: 300 }}>Your Bookings:</h3>
        <div style={{ display: 'grid', overflow: 'scroll' }}>
          {evalArray(bookings) === true
            ? (bookings as Booking[]).map((booking: Booking) => {
                return (
                  <div
                    key={booking.id}
                    style={{
                      display: 'flex',
                      flexDirection: 'column',
                      fontSize: '1em',
                      color: 'white',
                      borderTop: '1px solid #a67b5b',
                      borderBottom: '1px solid #a67b5b',
                      paddingLeft: '.5em',
                    }}
                  >
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>Booking ID: {booking.id}</p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>Hotel ID: {booking.hotel_id}</p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>Room booked: {booking.room_id}</p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>
                      Booking status: {booking.booking_status}
                    </p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>
                      Booking start-end dates: {booking.start_date + ' - ' + booking.end_date}
                    </p>
                  </div>
                );
              })
            : evalObject(bookings) === true
            ? Array.of(bookings as Booking).map((booking: Booking) => {
                return (
                  <div
                    key={booking.id}
                    style={{
                      display: 'flex',
                      flexDirection: 'column',
                      fontSize: '1em',
                      color: 'white',
                      borderTop: '1px solid #a67b5b',
                      borderBottom: '1px solid #a67b5b',
                      paddingLeft: '.5em',
                    }}
                  >
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>Booking ID: {booking.id}</p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>Hotel ID: {booking.hotel_id}</p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>Room booked: {booking.room_id}</p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>
                      Booking status: {booking.booking_status}
                    </p>
                    <p style={{ lineHeight: 0, fontSize: '1em' }}>
                      Booking start-end dates: {booking.start_date + ' - ' + booking.end_date}
                    </p>
                  </div>
                );
              })
            : null}
        </div>
      </div>
      <div className='profile_main'>
        <h3 style={{ color: 'white', justifySelf: 'center', fontWeight: 300 }}>Your Invoices:</h3>
        {evalArray(invoices) == true
          ? (invoices as Invoice[]).map((invoice: Invoice) => {
              return (
                <div
                  key={invoice.id}
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    fontSize: '1em',
                    color: 'white',
                    borderTop: '1px solid #a67b5b',
                    borderBottom: '1px solid #a67b5b',
                    paddingLeft: '.5em',
                  }}
                >
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice ID: {invoice.id}</p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>Issued: {invoice.issued}</p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice total: ${invoice.total}</p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>
                    Invoice paid: {invoice.paid ? 'Paid' : 'Not paid'}
                  </p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>
                    Invoice payment date: {invoice.payment_date}
                  </p>
                </div>
              );
            })
          : evalObject(invoices) === true
          ? Array.of(invoices as Invoice).map((invoice: Invoice) => {
              return (
                <div
                  key={invoice.id}
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    fontSize: '1em',
                    color: 'white',
                    borderTop: '1px solid #a67b5b',
                    borderBottom: '1px solid #a67b5b',
                    paddingLeft: '.5em',
                  }}
                >
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice ID: {invoice.id}</p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>Issued: {invoice.issued}</p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>Invoice total: ${invoice.total}</p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>
                    Invoice paid: {invoice.paid ? 'Paid' : 'Not paid'}
                  </p>
                  <p style={{ lineHeight: 0, fontSize: '1em' }}>
                    Invoice payment date: {invoice.payment_date}
                  </p>
                </div>
              );
            })
          : null}
      </div>
      <div className='profile_footer'>
        <Footer />
      </div>
    </div>
  );
};

export default Profile;
