import './style.css';
import Homepage from './homepage/homepage';
import { Route, Routes } from 'react-router-dom';
import State from './state';
import Api from './api/api';
import Profile from './profile/profile';
import { keycloak, keycloakInit } from './keycloak';
import HotelView from './hotelview/hotelview';
import RoomView from './hotelview/roomview';

const App = () => {
  // TOKEN EXPIRATION IS 5 MINUTES
  Api.temporaryTokenLogger();

  // Is keycloak initialized?
  !keycloak.onReady ? keycloakInit() : null;

  console.log('State::fetchStateByKey ', State.fetchStateByKey('cities'));
  console.log('State.fetchStateByKey :: boolean -> ', State.fetchStateByKey('cities') === null);

  if (State.fetchStateByKey('cities') === null) {
    (async () => {
      Api.findCities().then((res) =>
        res.json().then((cities) => State.storeStateToLocalStorage('cities', cities))
      );
    })();
  }

  return (
    <>
      <Routes>
        <Route path='/' element={<Homepage />} />
        <Route path='results' element={<HotelView />} />
        <Route path='results/rooms/:hotelid' element={<RoomView />} />
        <Route path='profile' element={<Profile />} />
      </Routes>
    </>
  );
};

export default App;
