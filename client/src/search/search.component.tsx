import { ChangeEvent, useState, useRef, useEffect } from 'react';
import './search.css';
import { BsSearch } from 'react-icons/bs';
import { GiShoppingBag } from 'react-icons/gi';
import { IoMdSearch } from 'react-icons/io';

interface ISearch {
  scrollEvent: boolean;
}

const Search = ({ scrollEvent }: ISearch): JSX.Element => {

  const ref = useRef<HTMLDivElement>(null);
  const ref2 = useRef<HTMLInputElement>(null);
  const [isOpen, setOpen] = useState(false);

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

  return (
    <form
      style={scrollEvent ? { transform: 'translateY(-120%)' } : {}}
      className='search'
      role='search'
    >
      <div className='search__wrapper'>
        <div className='search__wrapper__left'>
          <IoMdSearch id='searching-icon' />
          <input
            ref={ref2}
            type='search'
            placeholder='Search..'
            name='search'
            id='searchInput'
            aria-label='Write a keyword for search'
            maxLength={75}
          />
        </div>
      </div>
      <nav
        ref={ref}
        className='search__suggestions'
        style={isOpen ? { maxHeight: '35.3rem', height: 'auto' } : {}}
      >
      </nav>
    </form>
  );
};

export default Search;
