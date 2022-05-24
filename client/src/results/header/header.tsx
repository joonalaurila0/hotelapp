import * as React from 'react';
import Search from '../../search/search.component';
import { AiOutlineFileSearch } from 'react-icons/ai'
import { BsCalendarCheck } from 'react-icons/bs'

interface ResultArgs {
  results?: number;
}

const ResultHeader = ({ results = 0 }: ResultArgs ) => {
  const [scrollDir, setScrollDir] = React.useState(false);
  const getDate = () => {
    return String(new Date().getDate()).padStart(2, '0') 
      + '/' 
      + String(new Date().getMonth() + 1).padStart(2, '0') 
      + '/'
      + new Date().getFullYear();
  }
  const getTime = () => {
    return new Date()
      .getHours() 
      + ":" 
      + new Date().getMinutes() 
      + ":" 
      + new Date().getSeconds();
  }
  return (
    <div className='results_header'>
      <div className='results_header_info'>
        <div>
          <AiOutlineFileSearch style={{ color: 'white', fontSize: '1.8rem', marginLeft: '0.5rem' }}/>
          <p style={{ color: 'white' }}>Hotels found: {results}</p>
        </div>
        <div>
          <BsCalendarCheck style={{ color: 'white', fontSize: '1.8rem', marginRight: '0.5rem' }}/>
          <p style={{ color: 'white' }}>Date: {getDate()} </p>
        </div>
        <div>
          <BsCalendarCheck style={{ color: 'white', fontSize: '1.8rem', marginRight: '0.5rem' }}/>
          <p style={{ color: 'white' }}>Time: {getTime()} </p>
        </div>
      </div>
      <div className='results_header_search'>
        <Search scrollEvent={scrollDir} button={false} />
      </div>
    </div>
  )
}

export default ResultHeader;
