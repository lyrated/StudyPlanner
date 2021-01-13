# StudyPlanner
simple android app to create lists of topics to study. exchange topics via bluetooth - implemented with [ASAP library](https://github.com/SharedKnowledge/ASAPAndroid/wiki).

At first start you have to add subjects first (view subjects with the second menu item).
Then you can add your topics with name, date when to study, which subject and how long you plan to study.

### Study partner
To use the Study Partner feature, select the 1st menu item. Start bluetooth. If your study partner has also bluetooth turned on, this screen displayed and you paired with them before, their app ID will display as a button.
Select the topics you want to send and click on your study partner's ID. The topics will automatically be saved.

Created for Mobile Development course at HTW Berlin.

Some things that could be added:
- Archive: display all old topics.
- Complete topics: when completed, move automatically to archive. If not completed yet on the date you wanted to study, move automatically to next day.
- Statistics: show the duration, average,... of all subjects studied.
- Validation: change the date field into a calendar and validate the text inputs.
- Discover new devices: show all bluetooth devices nearby and pair with them to send your topics.
- ... and so much more!
