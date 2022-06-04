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

  console.log("keycloak initialized ::", keycloak.onReady);
  console.log("keycloak authenticated ::", keycloak.authenticated);
  console.log("keycloak profile ::", State.fetchStateByKey('profile'));
  console.log("keycloak token ::", keycloak.token);
  console.log("keycloak parsed token ::", keycloak.tokenParsed);

  // TOKEN EXPIRATION IS 5 MINUTES
  Api.temporaryTokenLogger();
  
  // Is keycloak initialized?
  !keycloak.onReady 
    ? keycloakInit() 
    : null;

  // If state can't find hotels by key
  async function gibCities() {
    // Checks for a key: 'cities' in localStorage
    if (!State.fetchStateByKey('cities')) {
      Api.findCities()
        .then((res) => res.json()
          .then(cities => State.storeStateToLocalStorage('cities', cities)));
    }
  };

  !State.fetchStateByKey('cities') 
    ? gibCities().catch((err) => new Error("Request could not be made to fetch the cities, " + err)) 
    : null;
  
  return (
    <>
      <Routes>
        <Route path='/' element={<Homepage />} />
        <Route path='results' element={<HotelView />} />
        <Route path='results/rooms/:hotelid' element={<RoomView/>} />
        <Route path='profile' element={<Profile/>} />
      </Routes>
    </>
  );
};

export default App;
