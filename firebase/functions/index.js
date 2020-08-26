const app = require('express')();
const functions = require('firebase-functions');
const FBAuth = require('./helpers/fbAuth');
const { signup, login, getUserInfo } = require('./handlers/users');
const { createNewCluster, getCluster, joinClusterByCode } = require('./handlers/clusters');
const { createQuestion, answerQuestion, getQuestion } = require('./handlers/questions');
const { createMeeting, getMeeting } = require('./handlers/meetings');

app.get('/users/:uid', getUserInfo);

app.post('/signup', signup);
app.post('/login', login);

app.post('/createCluster', FBAuth, createNewCluster);
app.get('/clusters/:uid', getCluster);
app.post('/joinClusterCode/:uid', FBAuth, joinClusterByCode);

app.post('/questions', createQuestion);
app.post('/answerQuestion', FBAuth, answerQuestion);
app.get('/questions/:uid', getQuestion);

app.post('/meetings', FBAuth, createMeeting);
app.get('/meetings/:uid', getMeeting);

exports.api = functions.https.onRequest(app);
