import './header.css';

const Header = () => {
  return (
    <div className='header'>
      <div className='logo'>
        <h1 className='logo' style={{ color: '#d3cfca' }}>
          Hotely
        </h1>
        <a href='#deals' className='logo'>
          Deals
        </a>
        <a href='#about' className='logo'>
          About
        </a>
      </div>
    </div>
  );
};

export default Header;
