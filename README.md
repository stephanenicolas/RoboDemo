RoboDemo
========

RoboDemo is a ShowCase library for Android to demonstrate to users how a given Activity works.
Additionally use RoboDemo to walk a user through a first time introduction to the app or show them how to use a new feature.

RoboDemo is not maintained anymore. It works but you may find more convenient to use a maintained library in : 
https://github.com/amlcurran/ShowcaseView


Screenshots 
-----------

Here is an example (from the sample application) :

![Activity under showcase](https://github.com/stephanenicolas/RoboDemo/raw/master/docs/activity-under-showcase-small.png "Activity under showcase")
&nbsp;
![Activity showcase animation step 1](https://raw.github.com/stephanenicolas/RoboDemo/master/docs/activity-showcase-animation-1-small.png "Activity showcase animation step 1")
&nbsp;
![Activity showcase animation step 2](https://raw.github.com/stephanenicolas/RoboDemo/master/docs/activity-showcase-animation-2-small.png "Activity showcase animation step 2")

From left to right : 

1. activity under show case
2. activity showcase, first step of animation
3. activity showcase, second & final step of animation

Overview
--------

There are some cases where applications require more complex interactions from users,
or new interactions not covered by Android UI Guidelines.

RoboDemo eases creating showcases / explaining / demonstrating of such activities to users.
It will display an overlay to illustrate the `Activity` under showcase. The explanations consist of a series of
points to click on and their associated labels. The  `Activity` under showcase is dimmed and the showcase highlights
transparent areas to point views or positions users have to click.

Creation of `DemoFragment` is straightforward, have a look at the sample to put RoboDemo in your own app :

1. create a `DemoFragment`, use a custom xml layout
2. in the `Activity` undershowcase, pass views or coordinates and their associated labels. 

RoboDemo can also walk a user through your own app. Great for first time use or explaining a feature that is not used often. Look in the sample to see how to switch from a showcase to a walkthrough.

To learn more, visit [RoboDemo Fragment Starter Guide](https://github.com/ericharlow/RoboDemo/wiki/RoboDemo-FragmentStarterGuide) and [browse RoboDemo Javadocs online](http://stephanenicolas.github.com/RoboDemo/apidocs/index.html).

Customization 
-------------

RoboDemo can be customized in different ways : 
* using a custom drawable
* using transparency or not to highlight the areas to click on
* using custom `Paint` to render labels, and the area under labels
* using custom drawable and text locations
* and some more for sure...

To learn more, visit [RoboDemo Fragment Starter Guide](https://github.com/ericharlow/RoboDemo/wiki/RoboDemo-FragmentStarterGuide) and [browse RoboDemo Javadocs online](http://stephanenicolas.github.com/RoboDemo/apidocs/index.html).

Modules 
-------

* a library that can be used inside Eclipse (with or without m2e)
* a sample application

Configuration
-------------

* In maven : just deploy the library in your nexus, and copy sample configuration
* In Eclipse :
  * Without m2e : unzip the library in the same workspace as your project, and use the library as an android library
  * With m2e : unzip the library in the same workspace as your project, and copy dependencies in the pom.xml file of the sample 
  

License
=======

    Copyright 2012 Stéphane NICOLAS

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


History
-------

RoboDemo has been initiated during a project for [Octo Technology](http://www.octo.com) as we implemented an Activity in which user had to move files in the filesystem.
This kind of interaction is finally unusual in android and almost every file explorer app uses its custom workflow to achive it.

We decided to implement some kind of tutorial to explain our idea (basically, inspired from OI File Manager).

We asked a question about it [on Stack Over Flow](http://stackoverflow.com/q/12148381/693752) and were redirected to 
[Espiandev / ShowcaseView](https://github.com/Espiandev/ShowcaseView). We finally decided to use our own solution, and it became a 
lib in September 2012 : RoboDemo.

Alternative
------------

You can also visit [Espiandev / ShowcaseView](https://github.com/Espiandev/ShowcaseView). This project is mature and clean as well.
It provides a "native" feeling of tutorial for apps.

RoboDemo in the News !!
-----------------------

* [Android dev weekly, issue #49] (http://androiddevweekly.com/2013/03/11/Issue-49.html?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed%3A+AndroidDevWeekly+%28%23AndroidDev+Weekly%29)
