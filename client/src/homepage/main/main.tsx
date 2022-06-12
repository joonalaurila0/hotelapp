import './main.css';
import Header from '../header/header';
import Search from '../../search/search';
import { keycloak } from '../../keycloak';
import { KeycloakProfile } from 'keycloak-js';
import Api, { BookingStatus } from '../../api/api';
import State from '../../state';
import { ISO8601Date } from '../../util';

const Main = () => {
  const signIn = () => keycloak.login();

  async function setUserState() {
    keycloak.loadUserProfile().then((profile) => {
      console.info('KC Profile: ', profile);
      State.storeStateToLocalStorage('profile', profile);
    });
  }

  // calls the Keycloak class to get the token
  const mockTest = () => {
    console.group('KC Token: ', keycloak.token);
    console.group('KC TokenParsed: ', keycloak.tokenParsed);
    console.group('KC is token expired? => ', keycloak.isTokenExpired());
    console.group('KC Auth flow: ', keycloak.flow);
    console.group('KC Profile => ', keycloak.loadUserProfile());
    setUserState();
  };

  async function testBooking() {
    const profile: KeycloakProfile = State.fetchStateByKey('profile');
    if (profile && profile.id) {
      const res: Response = await Api.createBooking({
        customer_id: profile.id,
        hotel_id: 5,
        room_id: 5,
        booking_status: BookingStatus.Pending,
        start_date: new Date().toISOString(),
        end_date: ISO8601Date(7),
      });
      const booking = await res.json();
      console.log('Booking created :: ', booking);
      State.storeStateToLocalStorage(`bookings`, booking);
    }
  }

  async function testInvoice() {
    const profile: KeycloakProfile = State.fetchStateByKey('profile');
    if (profile && profile.id) {
      const res: Response = await Api.createInvoice({
        bookingId: '9c401ad2-3fe9-4bfb-b19d-0356713e52ff',
        customerId: profile.id,
        total: 45.5,
        issued: new Date().toJSON(),
        payment_date: ISO8601Date(),
        paid: false,
        cancelled: false,
      });
      const invoices = await res.json();
      console.log('Invoice created :: ', invoices);
      State.storeStateToLocalStorage(`invoices`, invoices);
    }
  }

  // Functions that run to check if user exists in the local db
  async function afterSignIn() {
    const profile: KeycloakProfile = State.fetchStateByKey('profile');
    if (profile && profile.id && profile.email) {
      const res = await Api.findCustomer(profile.id);
      console.log('afterSignIn :: res:', res);

      if (res.status == 404) {
        console.log('USER WAS NOT FOUND');
        Api.createCustomer(profile.id, profile.email)
          .then((res) => {
            console.log('afterSignIn :: createCustomer ', res);
            console.log('afterSignIn :: User succesfully created', res.json());
          })
          .catch((e) => console.error('afterSignIn :: Error: ', e));
      }

      if (res.status == 200 || res.status == 202) {
        console.log('afterSignIn :: User was found', res);
      }
    }
  }

  async function fetchInvoices() {
    const profile: KeycloakProfile = State.fetchStateByKey('profile');
    if (profile && profile.id) {
      const invoices = await Api.findInvoices('b31755e0-505d-4ea9-ae1e-6a38052e55eb');
      const res = invoices.json();
      console.log('Invoices of a customer ::', res);
    }
  }

  async function fetchBookings() {
    const profile: KeycloakProfile = State.fetchStateByKey('profile');
    if (profile && profile.id) {
      const bookings = await Api.findBookings('b31755e0-505d-4ea9-ae1e-6a38052e55eb');
      const res = bookings.json();
      console.log('Bookings of a customer ::', res);
    }
  }

  async function fetchCustomers() {
    const customers = await Api.findCustomers();
    const res = customers.json();
    console.log('Customers ::', res);
  }

  // Access protected resource using backend token
  // from the keycloak object and pass it over when you make HTTP requests.
  async function tokenTester() {
    const token = State.fetchStateByKey('token');
    fetch(
      `http://localhost:8072/hotel-service/bookings/customer/673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9`,
      {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      }
    ).then((res) => console.log('tokenTester ::', res));
  }

  async function isProfileExistent() {
    const profile: KeycloakProfile = State.fetchStateByKey('profile');
    const token = State.fetchStateByKey('token');
    const req = new XMLHttpRequest();
    req.onreadystatechange = () => {
      if (req.readyState === 4) {
        console.log('isProfileExistent ::', req);
        if (req.status === 404 && profile.id && profile.email) {
          Api.createCustomer(profile.id, profile.email)
            .then((res) => {
              console.log('afterSignIn :: createCustomer ', res);
              console.log('afterSignIn :: User succesfully created', res.json());
            })
            .catch((e) => console.error('afterSignIn :: Error: ', e));
        }
      }
    };
    req.open('GET', `http://localhost:8072/customer-service/customers/${profile.id}`);
    req.setRequestHeader('Authorization', `Bearer ${token}`);
    req.send();
  }

  const authSpec = () => {
    Api.temporaryTokenLogger();
  };

  return (
    <div className='main'>
      <div className='squaresfirst'>
        <Header />
      </div>
      <div className='squares'>
        <div>
          <h1 style={{ width: '100%' }}>Hotely finds you the hotel you want</h1>
        </div>
      </div>
      <div className='squares2' style={{ justifyContent: 'center' }}>
        <div className='innerSquare2'>
          <Search scrollEvent={true} />
        </div>
      </div>
      <div className='squares3'>
        <div className='contact__text'></div>
      </div>
      <div className='imageColumn'>
        <div className='imageColumn_WRAPPER'>
          <div className='imageColumn_AUTH'>
            <a style={{ cursor: 'pointer' }} onClick={() => signIn()} title='Sign in'>
              Sign in
            </a>
            <a style={{ cursor: 'pointer' }} onClick={() => mockTest()} title='Test keycloak'>
              Z
            </a>
            <a
              style={{ cursor: 'pointer' }}
              onClick={() => fetchCustomers()}
              title='Fetch customers'
            >
              A
            </a>
            <a style={{ cursor: 'pointer' }} onClick={() => testBooking()} title='Create booking'>
              B
            </a>
            <a style={{ cursor: 'pointer' }} onClick={() => testInvoice()} title='Create invoice'>
              G
            </a>
            <a style={{ cursor: 'pointer' }} onClick={() => afterSignIn()} title='Create customer'>
              F
            </a>
            <a
              style={{ cursor: 'pointer' }}
              onClick={() => fetchBookings()}
              title='Fetch customer bookings'
            >
              X
            </a>
            <a
              style={{ cursor: 'pointer' }}
              onClick={() => fetchInvoices()}
              title='Fetch customer invoices'
            >
              XX
            </a>
            <a
              style={{ cursor: 'pointer' }}
              onClick={() => tokenTester()}
              title='Test if token is valid'
            >
              XXX
            </a>
            <a
              style={{ cursor: 'pointer' }}
              onClick={() => isProfileExistent()}
              title='Does profile exist function'
            >
              E
            </a>
            <a style={{ cursor: 'pointer' }} onClick={() => authSpec()} title='see api.authToken'>
              C
            </a>
          </div>
          <div className='imageColumn_LANG_SELECT'>
            <div className='langbox' id='finflag'>
              <p>Suomeksi </p>
            </div>
            <div className='langbox' id='enflag'>
              <p>English</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Main;
