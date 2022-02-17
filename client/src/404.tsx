import Boat from '../html/static/404.png';

function NotFoundPage() {
  return (
    <div style={{ display: 'grid', placeItems: 'center center' }}>
      <img src={Boat} />
      <h1 style={{ fontSize: '2.4rem', color: '#5B6BFF', fontFamily: 'Lato' }}>Page could not be found!</h1>
    </div>
  )
}

export default NotFoundPage;
