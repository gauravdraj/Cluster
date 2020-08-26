const { admin, db } = require('../helpers/admin');

// I assume the user logged in is owner
exports.createMeeting = (req, res) => {
    const newMeeting = {
        cluster: req.body.cluster,
        active: req.body.active,
        questions: []
    };

    var oldData = {};

    db.doc(`/clusters/${req.body.cluster}`).get().then(doc => {
      oldData = doc.data().meetings;
      return;
    }).catch(err => {
      console.error(err);
      res.json({ error: "cluster probably doesn't exist" });
    });

    db.collection('meetings').add(newMeeting).
    then(doc => {
        const resMeeting = newMeeting;
        resMeeting.meetingId = doc.id;

        // Add ID to user clusters array
        oldData.push(resMeeting.meetingId);

        var newJson = {};
        newJson['meetings'] = oldData;

        // Edit clusters of user to include new cluster ID
        db.collection('clusters').doc(req.body.cluster).update(newJson).catch(err => {
          console.log(err)
        });

        res.json({ message: `meeting ${doc.id} created successfully` });
        return;
    }).catch(err => {
        res.status(500).json({ error: `something went wrong - does the given cluster exist and is the current user the owner?` });
        console.error(err);
    });
};

exports.getMeeting = (req, res) => {
    let meetingData = {};
    db.doc(`/meetings/${req.params.uid}`).get().then(doc => {
        if(!doc.exists)
            return res.status(404).json({ error: 'Meeting not found' });

        meetingData = doc.data();
        meetingData.uid = doc.id;
        return db.collection('meetings').where('uid', '==', req.params.uid).get();
    }).then(data => {

        return res.json(meetingData);
    }).catch(err => {
        console.error(err);
        return res.status(500).json({ error: err.code });
    });
};
