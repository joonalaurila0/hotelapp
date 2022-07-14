// returns ISO8601 Date (Example: 2022-06-01T19:11:21.941Z)
// Splits it into an array by 'T' as the pattern -> [ '2022-06-01', '19:11:53.415Z' ]
// Gets the first item in the array -> '2022-06-01'

/**
 * @returns Short ISO8601 Date string.
 */
const ISO8601Date = (day = 0, month = 0, year = 0): string => {
  const now = new Date();
  return new Date(now.getFullYear() + year, now.getMonth() + month, now.getDate() + day)
    .toISOString()
    .split('T')[0];
};

/**
 * Gives current time and date.
 * @returns Date string that conforms to a timestamp in yyyy-mm-dd hh:mm:ss format.
 */
const TimestampNow = (): string => {
  return (
    new Date().toLocaleDateString('en-US', { year: 'numeric' }) +
    '-' +
    new Date().toLocaleDateString('en-US', { month: '2-digit' }) +
    '-' +
    new Date().toLocaleDateString('en-US', { day: '2-digit' }) +
    ' ' +
    new Date().getHours().toLocaleString() +
    ':' +
    new Date().getMinutes().toLocaleString() +
    ':' +
    new Date().getSeconds().toLocaleString()
  );
};

// Evaluates to Array<Type> if true
/** @returns true if is Array type AND not null. */
const evalArray = (array: any): boolean => {
  return Array.isArray(array) && array != null;
};

/** @returns true if is NOT Array type AND not null. */
const evalObject = (obj: any): boolean => {
  return !Array.isArray(obj) && obj != null;
};

export { ISO8601Date, TimestampNow, evalObject, evalArray };
