# Firebase functions for Cluster

## Signup
url:  https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/signup

POST request

json input:
```
{
	"name": "John Smith",
	"email": "JohnSmith@email.com",
	"password": "password123"
}
```

## Create cluster
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/createCluster

POST request

json input:
```
{
	"active": true,
	"name": "Cluster Name",
	"password": "password (blank if none)"
}
```

MUST BE AUTHENTICATED - a user token needs to be added as a header (key = Authorization, value = "Bearer KEY"). Keys are returned when a user signs up, although I think they expire at some point (60 mins?).

This also adds the cluster ID to the ownerOf field of the user object. The ownerEmail is also updated in the cluster object to show who the owner is.

## Join cluster with code
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/joinClusterCode/:uid where ":uid" is the join code (5 character string)


POST request. No json input.

MUST BE AUTHENTICATED - a user token needs to be added as a header (key = Authorization, value = "Bearer KEY"). Keys are returned when a user signs up, although I think they expire at some point (60 mins?).

This also updates the clusters field of the currently logged in user. Also updates the members field of the given cluster with the new user.

## Get user info
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/users/:uid where ":uid" is the id of a given user

GET request

Probably should add some authentication to this. No input.

## Create question
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/questions

json input:
```
{
  "answerChoices": ["array of answer choices"],
  "prompt": "prompt of question",
  "meetingId": "id of meeting for which this is being added to",
  "type": "type of input expected",
  "submissionType": "submission types accepted"
}
```

Also updates the questions field in the given meeting with the new question.

## Get question
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/questions/:uid where ":uid" is the id of a given question

GET request

Probably should add some authentication to this. No input.

## Answer question
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/answerQuestion

POST request

json input:
```
{
  "question": "uid of question being answered",
  "answer": "user's answer"
}
```

MUST BE AUTHENTICATED - a user token needs to be added as a header (key = Authorization, value = "Bearer KEY"). Keys are returned when a user signs up, although I think they expire at some point (60 mins?).

Saves answer into the question object in the userAnswer array in the format "email - answer". Currently does not check for duplicates meaning a single user could have multiple answers.  

## Create meeting
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/meetings

json input:
```
{
	"cluster": "unique id of cluster associated",
	"active": true
}
```

Also updates the meetings field in the given cluster with the new meeting.

## Get meeting
url: https://us-central1-cluster-hackathon-group.cloudfunctions.net/api/meetings/:uid where ":uid" is the id of a given meeting

GET request

Probably should add some authentication to this. No input.
