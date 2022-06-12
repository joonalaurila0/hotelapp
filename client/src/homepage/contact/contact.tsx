import './contact.css';
import { RowField, ColumnRow, InputField, TextareaField } from '../form-components/form-components';

// This was for form evaluation.
//interface FormValues {
//  firstname: string;
//  lastname: string;
//  email: string;
//  street: string;
//  zipcode: string;
//  city: string;
//  additionalinfo: string;
//}

const Contact = () => {
  const onSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    alert('Submit succesful, thank you contact.')
  }
  return (
    <div className='contact' id='contact'>
      <h1>Contact and Customer Support</h1>
      <div />
      <div className='contactform' style={{ justifySelf: 'center' }}>
        <div className='container'>
          <form onSubmit={onSubmit}>
            <RowField>
              <ColumnRow>
                <InputField field={'First name'} />
              </ColumnRow>
              <ColumnRow>
                <InputField field={'Last name'} />
              </ColumnRow>
            </RowField>
            <RowField>
              <ColumnRow>
                <InputField field={'Email'} />
              </ColumnRow>
              <ColumnRow>
                <InputField field={'Street'} />
              </ColumnRow>
            </RowField>
            <RowField>
              <ColumnRow>
                <InputField field={'Zip code'} />
              </ColumnRow>
              <ColumnRow>
                <InputField field={'City'} />
              </ColumnRow>
            </RowField>
            <RowField>
              <ColumnRow>
                <TextareaField field={'Additional Info'} />
              </ColumnRow>
            </RowField>
            <input
              className='container__submit'
              name='submit_button'
              type='submit'
              value='Submit'
              style={{
                width: '90%',
                alignSelf: 'center',
              }}
            />
          </form>
        </div>
      </div>
    </div>
  );
};

export default Contact;
