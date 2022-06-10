import './contact.css';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { RowField, ColumnRow, InputField, TextareaField } from '../form-components/form-components';

interface FormValues {
  email: string;
  name: string;
  message: string;
}

const validationSchema = Yup.object({
  name: Yup.string().required('Required'),
  email: Yup.string().email('Invalid email').required('Required'),
  message: Yup.string().required('Required'),
});

const Contact = () => {
  const formik = useFormik<FormValues>({
    initialValues: {
      email: '',
      name: '',
      message: '',
    },
    validationSchema: validationSchema,
    onSubmit: (values: FormValues) => {
      alert(JSON.stringify(values, null, 2));
    },
  });
  return (
    <div className='contact' id='contact'>
      <h1>Contact and Customer Support</h1>
      <div />
      <div className='contactform' style={{ justifySelf: 'center' }}>
        <div className='container'>
          <form action=''>
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
