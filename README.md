# Cluster
Community Polling Made Easy. Submitted to [HackIllinois HackThis ](https://hackthis.hackillinois.org/)
## Inspiration
During a conversation with his high school teacher, one of our group members realized that one of the most prevalent problems with online learning is that the teacher no longer can read the room as well as when they could when teaching in person. It’s this lack of communication that made teaching such a struggle for many who relied on scanning the classroom to gauge student interest and understanding.

In the classroom, adaptability is key -- teachers can feed off the energy in an in-person classroom and change their teaching strategy accordingly. If the students look bored, the teacher can take a break or add activities to make lectures more exciting. If the students are writing frantically, the teacher can slow down by extending a deadline. In general, teaching becomes much more effective when a clear line of communication exists between the teacher and the students’ feelings.

The current solution to this problem presented by online teaching frameworks are Zoom polls, which require a paid plan and administrative privileges, essentially restricting their use to larger institutions. In addition, Zoom Polls only use multiple choice or single answer questions. The resulting reports can also be adjusted to either give out users’ names or report them all anonymously, simultaneously creating problems of verification and user privacy.

The other popular alternative is Kahoot, which is a very short-term version of polling designed as a game. Kahoots suffer from requiring re-joining  in order to be reused and limited reports beyond their point system. Additionally, they value speed over accuracy and only use multiple choice questions, which limits their usefulness to review activities.

Naturally, the optimal environment for this result is an in-person classroom, and while we cannot perfectly replicate that,  we can do our best to close the loop so teachers know what students are feeling.

## What Cluster does

Cluster allows for simple, real time group polling, where questions can be posed live or daily. A cluster is structured with an admin and members, where the admin poses multiple choice or short answer questions and receives responses.

Live Polling: The crown jewel of Cluster! Teachers or leaders can post real-time questions for students to answer. These can be used simultaneously during an online lecture to gauge student comprehension and interest.

Daily Polling: Similar to Live Polling, but questions are posted for students to answer within the span of 24 hours. This feature can be used to ask about the quality of the day’s lecture or check if there was any material that was confusing.

Community tab: Allows posting of questions related to the Cluster and cannot be viewed by owners or teachers. The intent is to promote student to student communication by creating a space where they can discuss ideas or key notes. This large collaborative environment also reduces the need for students to search specific partners in their classes.
## How we built it
Our app is written in Java on the Android framework. We used a Firebase Firestore real-time database in order to keep track of data required to define and run the clusters. We used collections to model clusters, which contained separate collections for live and daily polls, along with the owner’s email and a list of viewers. Firebase also handled authorization, from logging in to changing usernames and passwords, which sped up the process of handling accounts.
## Challenges we ran into
For many of us, this was the first time that we used the Firebase database extensively and it took some of our backend developers more than a full day to understand how to use it properly. We also had issues with multi-threading in Android when updating several UI elements but resolved them in much less time.
## Accomplishments that we're proud of
We had never worked with Firebase or Android to such an extent before. At the start, we were just proud of every update we made to the database but over time we took on tougher challenges. One of these was implementing live events, which involved threading and retrieving data instantly from the cloud, and we consider our success there to be our biggest accomplishment.
## What we learned
Our team consisted of members with different levels of experience in app development. Some of us had never used Android Studio or interacted with a database before, while others had a decent amount of experience, just in the wrong fields.
Despite these discrepancies, however, we all managed to take away a few lessons from this hackathon. For one, the backend developers learned event handling in android- most of our transitions required sending data between activities. Planning those transmissions taught them a lot about design and optimization. We also learned that it’s often better to use libraries as opposed to coding features ourselves.
## What's next for Cluster
As we strive to make online learning more accessible, we are looking into various text-to-speech options to implement audible question reading and verbal submissions. This is mainly to appeal to smaller children (Pre-K through Grade two), who often have issues with typing. It would be fantastic to have a way to record and submit MP3 files instead of typing.
We would also love to make Cluster cross-platform, namely a web-client that would allow all users to continue to build community and interact online.

https://devpost.com/software/cluster-g6z0v1

[Video Demo](https://www.youtube.com/watch?v=7uwyFWmb3Vc)
