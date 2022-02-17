import * as React from 'react';
import './form-components.css';

interface inputField { field: string }

/**
 * Example: argument in form "First Name", gets processed to id "firstname".
 * This argument gets assigned as the id for input and textarea and the label
 * uses it the "for" attribute to match the id.
 */
const InputField = ({ field }: inputField) => {
  const [state, setState] = React.useState<{ active: Boolean, input: string }>({ active: false, input: '' });
  const onType = (event: React.ChangeEvent<HTMLInputElement>) => setState({ ...state, input: event.target.value });
  /* removes whitespace and lowercases */
  let fieldStr = field.replace(/\s+/, "").toLowerCase();
  return (
    <div 
      className={ state.active ? 'form-field form-field--is-active' 
      : state.input.length > 0 ? 'form-field form-field--is-filled' :
      'form-field' } 
      onFocus={() => setState({ ...state, active: true })} 
      onBlur={() => setState({ ...state, active: false })}
    >
      <div className="form-field__control">
        <label htmlFor={fieldStr} className='form-field__label'>{field}</label>
        <input id={fieldStr} type="text" className='form-field__input' value={state.input} onChange={onType} />
      </div>
    </div>
  );
}

/**
 * Argument in form "First Name", gets processed to id "firstname"
 */
const TextareaField = ({ field }: inputField) => {
  const [state, setState] = React.useState<{ active: Boolean, input: string }>({ active: false, input: '' });
  const onType = (event: React.ChangeEvent<HTMLTextAreaElement>) => setState({ ...state, input: event.target.value });
  /* removes whitespace and lowercases */
  let fieldStr = field.replace(/\s+/, "").toLowerCase();
  return (
    <div
      className={ state.active ? 'form-field form-field--is-active' 
      : state.input.length > 0 ? 'form-field form-field--is-filled' :
      'form-field' } 
      onFocus={() => setState({ ...state, active: true })} 
      onBlur={() => setState({ ...state, active: false })}
    >
      <div className="form-field__control">
        <label htmlFor={fieldStr} className='form-field__label'>{field}</label>
        <textarea id={fieldStr} className='form-field__textarea' value={state.input} onChange={onType} />
      </div>
    </div>
  );
}

const RowField = ({ children }: JSX.ElementChildrenAttribute) => {
  return (
    <div>{children}</div>
  );
}

const ColumnRow = ({ children }: JSX.ElementChildrenAttribute) => {
  return (
    <div className="col-sm">{children}</div>
  );
}

const Testarea = ({ children }: JSX.ElementChildrenAttribute) => {
  return (
    <div className="container">
      <form action="">
        {children}
      </form>
    </div>
  );
}

export { Testarea, ColumnRow, RowField, TextareaField, InputField };
