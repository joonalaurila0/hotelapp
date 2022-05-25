import { AiOutlineFileSearch } from "react-icons/ai";
import { AiOutlineMail } from "react-icons/ai";
import { VscAccount } from 'react-icons/vsc';
import { GoHome } from 'react-icons/go';


const ProfileHeader = () => {
  const scrollDir = () => true;
  return (
    <div className='profile_header'>
      <div className='profile_header_info'>
        <div>
          <GoHome style={{ color: 'white', fontSize: '2.2rem', marginLeft: '0.5rem' }}/>
          <h4 style={{ color: 'white', fontSize: '1.8rem', fontWeight: 300, textDecorationLine: 'underline' }}><a href='/'>Back to homepage</a></h4>
        </div>
        <div>
          <AiOutlineMail style={{ color: 'white', fontSize: '1.8rem', marginRight: '0.5rem' }}/>
          <p style={{ color: 'white' }}>Email: {} </p>
        </div>
        <div>
          <VscAccount style={{ color: 'white', fontSize: '1.8rem', marginRight: '0.5rem' }}/>
          <p style={{ color: 'white' }}>ID: {} </p>
        </div>
      </div>
      <div className='profile_header_search'>
      </div>
    </div>
  );
};

export default ProfileHeader;
