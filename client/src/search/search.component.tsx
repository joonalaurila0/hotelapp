import { ChangeEvent, useState, useRef, useEffect } from 'react';
import './search.css';
import { IoMdSearch } from 'react-icons/io';
import Api from '../api/api';
import State from '../state';
import { useNavigate } from 'react-router';


// Button defines whether component includes a button or not.
interface ISearch {
  scrollEvent: boolean;
  button?: boolean;
}

interface City {
  id: number;
  name: string;
  region: string;
  population: number;
  lat: number;
  lng: number;
}

const Search = ({ scrollEvent, button = true }: ISearch): JSX.Element => {

  const ref = useRef<HTMLDivElement>(null);
  const ref2 = useRef<HTMLInputElement>(null);
  const [isOpen, setOpen] = useState(false);
  const [input, setInput] = useState({ search: '', focused: false });
  const [data, setData] = useState<null | City[]>(null);
  const navigate = useNavigate();

  useEffect(() => {
    function handleClickOutside(event: Event) {
      if (
        ref.current &&
        ref2.current &&
        !ref2.current.contains(event.target as Node) &&
        !ref.current.contains(event.target as Node)
      ) {
        setOpen(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside, true);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside, true);
    };
  }, [ref]);

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    data ? setOpen(true) : null;
    // fetches client state for matching keywords
    function fetchState() {
      let results = State.fetchStateByKey('cities')
      let output = results.filter((elem: City) => {
        return elem.name.match(new RegExp(input.search.trim(), 'gi'));
      })
      return output ? output : null;
    }
    fetchState() ? setData(fetchState()) : null;
    const { name, value } = e.target;
    setInput((input) => ({ ...input, [name]: value }));
  };

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    // Stores the response from API to client state if 'hotels' key does not exists on LocalStorage.
    if (!State.fetchStateByKey('hotels')) {
      Api.findHotels().then((res) => {
        res.json().then((hotels) => 
          State.storeStateToLocalStorage('hotels', hotels)).then(() => navigate('/results'))
      })
    }
    if (input.search && State.fetchStateByKey('hotels')) {
      navigate('/results');
    }
  }

  return (
    <form
      style={scrollEvent ? { transform: 'translateY(-120%)' } : {}}
      className='search'
      role='search'
      onSubmit={handleSubmit}
    >
      <div className='search__wrapper'>
        <div className='search__wrapper__left'>
          <IoMdSearch id='searching-icon' />
          <input
            ref={ref2}
            onFocus={() => setInput({ ...input, focused: true })}
            onBlur={() => setInput({ ...input, focused: false })}
            onChange={handleChange}
            value={input.search}
            type='search'
            placeholder='Search..'
            name='search'
            id='searchInput'
            list='city-list'
            aria-label='Write a keyword for search'
            maxLength={75}
          />
          <datalist id='city-list'>
            {data && data.length > 0 ? (data.map((city: City) => {
              return (
                <option key={city.id} value={city.name}>{city.name}</option>
              );
            })) : (null)}
          </datalist>
        </div>
      </div>
      {button ? (
      <div className='search_button_wrapper'>
        <button style={{ justifySelf: 'center', fontSize: '1.2rem', padding: '1.1rem 2.5rem' }}>Search for hotels</button>
      </div>) : null
      }
    </form>
  );
};

export default Search;
