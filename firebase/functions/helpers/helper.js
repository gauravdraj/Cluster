const createRandID = () => {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, helperRand(c));
};

const helperRand = (c) => {
  var r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
  return v.toString(16);
}

const makeId = (length) => {
   var result           = '';
   var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
   var charactersLength = characters.length;
   for ( var i = 0; i < length; i++ ) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
   }
   return result;
}

const isEmpty = string => {
    if(string.trim() === '')
        return true;
    else
        return false;
};

const isEmail = email => {
    // const regEx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    // if(email.match(regEx))
    //     return true;
    // else
    //     return false;
    var re = /\S+@\S+\.\S+/;
    return re.test(email);
};

exports.validateSignUpData = (data) => {
    let errors = {};

    if(isEmpty(data.email))
        errors.email = 'Must not be empty';
    else if(!isEmail(data.email))
        errors.email = 'Must be a vaild email address';

    if(isEmpty(data.password))
        errors.password = 'Must not be empty';
    if(data.password !== data.confirmPassword)
        errors.confirmPassword = 'Passwords must be the same';
    if(isEmpty(data.handle))
        errors.handle = 'Must not be empty';
    if(isEmpty(data.firstName))
        errors.firstName = 'Must not be empty';
    if(isEmpty(data.lastName))
        errors.lastName = 'Must not be empty';

    return {
        errors,
        valid: Object.keys(errors).length === 0 ? true : false
    }
};
