import './style.css';
import Homepage from './homepage/homepage';
import { Route, Routes } from 'react-router-dom';
import State from './state';
import Api from './api/api';
import Profile from './profile/profile';
import { keycloakInit } from './keycloak';
import HotelView from './hotelview/hotelview';
import RoomView from './hotelview/roomview';

const App = () => {
  // Initialize keycloak
  keycloakInit();
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
