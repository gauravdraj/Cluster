const { admin, db } = require('../helpers/admin');

exports.createQuestion = (req, res) => {
    const newQuestion = {
        answerChoices: req.body.answerChoices,
        meetingId: req.body.meetingId,
        prompt: req.body.prompt,
        type: req.body.type,
        submissionType: req.body.submissionType,
        userAnswers: []
    };

    var oldData = {};
    db.doc(`/meetings/${req.body.meetingId}`).get().then(doc => {
      oldData = doc.data().questions;
      return;
    }).catch(err => {
      console.error(err);
      res.json({ error: "Meeting probably doesn't exist" });
    });

    db.collection('questions').add(newQuestion).
    then(doc => {
        const resQuestion = newQuestion;
        resQuestion.questionId = doc.id;

        // Add ID to user clusters array
        if(oldData.length > 0)
          oldData.push(resQuestion.questionId);
        else
          oldData = [resQuestion.questionId];

        var newJson = {};
        newJson['questions'] = oldData;

        // Edit clusters of user to include new cluster ID
        db.collection('meetings').doc(req.body.meetingId).update(newJson).catch(err => {
          console.log(err)
        });

        res.json({ message: `Question ${doc.id} created successfully` });
        //res.json(oldData);
        return;
    }).catch(err => {
        res.status(500).json({ error: "Something went wrong" });
        console.error(err);
    });
};

exports.getQuestion = (req, res) => {
    let questionData = {};
    db.doc(`/questions/${req.params.uid}`).get().then(doc => {
        if(!doc.exists)
            return res.status(404).json({ error: 'Question not found' });

        questionData = doc.data();
        questionData.uid = doc.id;
        return db.collection('questions').where('uid', '==', req.params.uid).get();
    }).then(data => {

        return res.json(questionData);
    }).catch(err => {
        console.error(err);
        return res.status(500).json({ error: err.code });
    });
};

exports.answerQuestion = (req, res) => {
  var newItm = `${req.user.email} - ${req.body.answer}`;

  var newJson = {};
  newJson[req.user.email] = req.body.answer;

  db.collection('questions').doc(req.body.question).update({
    userAnswers: admin.firestore.FieldValue.arrayUnion(newItm)
  });

  return res.json({ success: "successful submission" });
}
