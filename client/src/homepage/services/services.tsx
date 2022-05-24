import './services.css'
 
const Services = () => {
  return (
    <div className='services' id='deals' style={{ width: '100%' }}>
      <h1 style={{ position: 'absolute', left: '30%' }}>Current deals and popular destinations</h1>
      <div className='servicebox'>
        <div style={{ background: 'transparent' }}></div>
        <div className='service-wrapper' style={{ background: 'rgba(0, 0, 0, 0.31)', display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)' }}>
          <div id='service-1' style={{}}><a>Look for a deal</a></div>
          <div id='service-2' style={{}}><a>Look for a deal</a></div>
          <div id='service-3' style={{}}><a>Look for a deal</a></div>
          <div id='service-4' style={{}}><a>Look for a deal</a></div>
        </div>
      </div>
    </div>
  )
}

export default Services;
