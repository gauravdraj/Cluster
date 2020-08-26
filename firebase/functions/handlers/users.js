const firebase = require('firebase');
const config = require('../config/config');
const { admin, db } = require('../helpers/admin');
const { createRandID, validateSignUpData } = require('../helpers/helper');

firebase.initializeApp(config);

exports.signup = (req, res) => {
  // TODO: convert email to lowercase here so that the document name has no capital letters

    const newUser = {
        name: req.body.name,
        email: req.body.email,
        password: req.body.password,
    };

    // const { valid, errors } = validateSignUpData(newUser);
    // if(!valid)
    //     return res.status(400).json(errors);

    let token, userId;

    db.doc(`/users/${req.body.email}`).get().then(doc => {
        if(doc.exists) {
            return res.status(400).json({ handle: 'this handle is already taken' });
        } else {
            return firebase.auth().createUserWithEmailAndPassword(newUser.email, newUser.password);
        }
    }).then(data => {
        userId = data.user.uid;
        return data.user.getIdToken();
    }).then(idToken => {
        token = idToken;
        const userCredentials = {
            email: newUser.email,
            name: newUser.name,
            clusters: [],
            ownerof: [],
            userId
        };

        return db.doc(`/users/${req.body.email}`).set(userCredentials);
    }).then(() => {
        return res.status(201).json({ token });
    }).catch(err => {
        console.error(err);
        if(err.code === 'auth/email-already-in-use') {
            return res.status(400).json({ email: 'Email is already in use' })
        } else {
            return res.status(500).json({ error: err.code });
        }
    });
};

exports.login = (req, res) => {
    const user = {
        email: req.body.email,
        password: req.body.password
    };

    // const { valid, errors } = validateLoginData(user);
    // if(!valid)
    //     return res.status(400).json(errors);

    firebase.auth().signInWithEmailAndPassword(user.email, user.password).then(data => {
        return data.user.getIdToken();
    }).then(token => {
        return res.json({token});
    }).catch(err => {
        console.error(err);

        if(err.code === 'auth/wrong-password')
            return res.status(403).json({ general: 'Wrong credentials - try again' });
        else
            return res.status(500).json({error: err.code});
    })
};

exports.getUserInfo = (req, res) => {
    let userData = {};
    db.doc(`/users/${req.params.uid}`).get().then(doc => {
        if(!doc.exists)
            return res.status(404).json({ error: 'Location not found' });

        userData = doc.data();
        userData.uid = doc.id;
        return db.collection('users').where('uid', '==', req.params.uid).get();
    }).then(data => {

        return res.json(userData);
    }).catch(err => {
        console.error(err);
        return res.status(500).json({ error: err.code });
    });
};
