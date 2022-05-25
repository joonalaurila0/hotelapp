import './style.css';
import Homepage from './homepage/homepage';
import { Route, Routes } from 'react-router-dom';
import Results from './results/results';
import State from './state';
import Api from './api/api';
import Profile from './profile/profile';

const App = () => {

  // If state can't find hotels by key
  async function gibCities() {
    let cache = [];
    let calls = 0;
    // Checks for a key: 'cities' in localStorage
    if (!State.fetchStateByKey('cities') && cache.length == 0 && calls < 1) {
      const cities = await Api.findCities()
      const response = await cities.json()
      calls++
      cache.push(response);
      State.storeStateToLocalStorage('cities', response);
    }
  };

  // Attempts to fetch cities json from localStorage
  !State.fetchStateByKey('cities') 
    ? gibCities().catch((err) => new Error("Request could not be made, " + err)) 
    : null;
  
  return (
    <>
      <Routes>
        <Route path='/' element={<Homepage />} />
        <Route path='results' element={<Results />} />
        <Route path='profile' element={<Profile/>} />
      </Routes>
    </>
  );
};

/* i was just testing type checking */
function makeState<S extends number | string = number>(arg: S) {
  return { arg }
};

export default App;
