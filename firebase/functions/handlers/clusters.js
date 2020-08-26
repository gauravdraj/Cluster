const { admin, db } = require('../helpers/admin');
//const { makeId } = require('../helpers/helper');

// Creates a new cluster with given info
exports.createNewCluster = (req, res) => {
    // Random code generation
    var length = 5;
    var result = '';
    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for(var i = 0; i < length; i++) {
       result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    // Items within each cluster document
    const newCluster = {
        active: req.body.active,
        meetings: [],
        members: [],
        name: req.body.name,
        ownerEmail: req.user.email,
        password: req.body.password,
        joinCode: result,
    };

    let oldData = [];

    db.doc(`/users/${req.user.email}`).get().then(doc => {
      if(doc.data().hasOwnProperty("ownerof"))
        oldData = doc.data().ownerof;
      return;
    }).catch(err => {
      console.error(err);
    });

    // Create a new document in the cluster collection
    db.collection('clusters').add(newCluster).
    then(doc => {
        const resCluster = newCluster;
        resCluster.clusterId = doc.id;

        var newJson = {}
        newJson[result] = doc.id;

        // Update the IDWITHCODES document to contain the joinCode - UID pair
        db.collection('joincodes').doc('IDWITHCODES').update(newJson).catch(err => {
          console.log(err)
        });

        if(oldData.length > 0)
          oldData.push(resCluster.clusterId);
        else
          oldData = [resCluster.clusterId];

        var uJson = {};
        uJson['ownerof'] = oldData;

        // Edit clusters of user to include new cluster ID
        db.collection('users').doc(req.user.email).update(uJson).catch(err => {
          console.log(err)
        });

        // Create collections within new cluster
        const clusterCollections = {
          question: "",
          emails: [],
          replies: []
        };
        db.collection('clusters').doc(resCluster.clusterId).collection('dailypolls').add({}).catch(err => console.error(err));
        db.collection('clusters').doc(resCluster.clusterId).collection('livepolls').doc('live').set(clusterCollections).catch(err => console.error(err));

        // Create new document in community collection
        const newPostCollection = {
          question: "New Question",
          replies: []
        };
        db.collection('community').doc(resCluster.clusterId).set({});
        db.collection('community').doc(resCluster.clusterId).collection('posts').add(newPostCollection).catch(err => console.error(err));

        res.json({ message: `cluster ${doc.id} created successfully`, shortCode: `${result}` });
        return;
    }).catch(err => {
        res.status(500).json({ error: `something went wrong` });
        console.error(err);
    });
};

// Returns json of desired cluster
exports.getCluster = (req, res) => {
    let clusterData = {};
    db.doc(`/clusters/${req.params.uid}`).get().then(doc => {
        if(!doc.exists)
            return res.status(404).json({ error: 'Cluster not found' });

        clusterData = doc.data();
        clusterData.uid = doc.id;
        return db.collection('clusters').where('uid', '==', req.params.uid).get();
    }).then(data => {

        return res.json(clusterData);
    }).catch(err => {
        console.error(err);
        return res.status(500).json({ error: err.code });
    });
};

// Called when user wants to join a new cluster based on its unique 5 character code
exports.joinClusterByCode = (req, res) => {
  let clusterData = {};

  let oldUserData = {};
  let oldClusterData = {};

  db.doc(`/users/${req.user.email}`).get().then(doc => {
    oldUserData = doc.data().clusters;
    return;
  }).catch(err => {
    console.error(err);
  });

  db.doc(`/joincodes/IDWITHCODES`).get().then(doc => {
      clusterData = doc.data();
      return db.collection('clusters').where('uid', '==', req.params.uid).get();
  }).then(data => {
      if(clusterData.hasOwnProperty(req.params.uid)) {
        let newId = clusterData[req.params.uid];

        db.doc(`/clusters/${newId}`).get().then(doc => {
          if(doc.data().hasOwnProperty("members"))
            oldClusterData = doc.data().members;
          return;
        }).catch(err => {
          console.error(err);
        });

        // Add ID to user clusters array
        oldUserData.push(newId);
        if(oldClusterData.length > 0)
          oldClusterData.push(req.user.email);
        else
          oldClusterData = [req.user.email];

        var newUserJson = {};
        var newClusterJson = {};
        newUserJson['clusters'] = oldUserData;
        newClusterJson['members'] = oldClusterData;

        // Edit clusters of user to include new cluster ID
        db.collection('users').doc(req.user.email).update(newUserJson).catch(err => {
          console.log(err)
        });

        db.collection('clusters').doc(newId).update(newClusterJson).catch(err => {
          console.log(err)
        });

        return res.json(newId);
      } else {
        return res.json({ error: "invalid code" });
      }
  }).catch(err => {
      console.error(err);
      return res.status(500).json({ error: err.code });
  });
};
