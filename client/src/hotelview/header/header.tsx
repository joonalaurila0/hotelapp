import Search from '../../search/search';
import { AiOutlineFileSearch } from 'react-icons/ai';
import { BsCalendarCheck } from 'react-icons/bs';
import { ISO8601Date } from '../../util';

interface ResultArgs {
  results?: number;
  description: string;
}

const ResultHeader = ({ results, description }: ResultArgs) => {
  return (
    <div className='results_header'>
      <div className='results_header_info'>
        <div>
          <AiOutlineFileSearch
            style={{ color: 'white', fontSize: '1.8rem', marginLeft: '0.5rem' }}
          />
          <p style={{ color: 'white' }}>
            {description ?? 'Undefined'} found: {results}
          </p>
        </div>
        <div>
          <BsCalendarCheck style={{ color: 'white', fontSize: '1.8rem', marginRight: '0.5rem' }} />
          <p style={{ color: 'white' }}>Date: {ISO8601Date()} </p>
        </div>
        <div>
          <BsCalendarCheck style={{ color: 'white', fontSize: '1.8rem', marginRight: '0.5rem' }} />
          <p style={{ color: 'white' }}>
            Time:{' '}
            {(() =>
              new Date().getHours() +
              ':' +
              new Date().getMinutes() +
              ':' +
              new Date().getSeconds())()}{' '}
          </p>
        </div>
      </div>
      <div className='results_header_search'>
        <Search scrollEvent={false} button={false} />
      </div>
    </div>
  );
};

export default ResultHeader;
