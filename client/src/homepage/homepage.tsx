import Navbar from './navbar/navbar';
import Main from './main/main';
import Services from './services/services';
import About from './about/about';
import Contact from './contact/contact';
import Footer from './footer/footer';

const Homepage = () => {
  return (
    <>
      <Navbar />
      <Main />
      <Services />
      <About />
      <Contact />
      <Footer />
    </>
  );
};

export default Homepage;
