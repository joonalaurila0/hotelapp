import './style.css';
import Homepage from './homepage/homepage';
import { Route, Routes } from 'react-router-dom';

const App = () => {
  console.group(makeState('hello'))
  return (
    <>
      <Routes>
        <Route path='/' element={<Homepage />} />
      </Routes>
    </>
  )
}

/* i was just testing type checking */
function makeState<S extends number | string = number>(arg: S) {
  return { arg }
}

export default App;
