import './services.css';

const Services = () => {
  return (
    <div className='services' id='deals' style={{ width: '100%' }}>
      <h1 style={{ position: 'absolute', left: '30%' }}>Current deals and popular destinations</h1>
      <div className='servicebox'>
        <div style={{ background: 'transparent' }}></div>
        <div
          className='service-wrapper'
          style={{
            background: 'rgba(0, 0, 0, 0.31)',
            display: 'grid',
            gridTemplateColumns: 'repeat(2, 1fr)',
          }}
        >
          <p
            style={{
              position: 'absolute',
              justifySelf: 'center',
              alignSelf: 'center',
              fontSize: '2em',
              color: 'white',
              fontWeight: 300,
            }}
          >
            Find hotel that you like!
          </p>
          <div id='service-1' style={{}} />
          <div id='service-2' style={{}} />
          <div id='service-3' style={{}} />
          <div id='service-4' style={{}} />
        </div>
      </div>
    </div>
  );
};

export default Services;
