RoboDemo
========

RoboDemo is a ShowCase library for Android to demonstrate to users how a given Activity works.

Screenshots 
-----------

Here is an example (from the sample application) :

*1.*![Activity under showcase](https://github.com/stephanenicolas/RoboDemo/raw/master/docs/activity-under-showcase-small.png "Activity under showcase")
&nbsp;
*2.*![Activity showcase animation step 1](https://raw.github.com/stephanenicolas/RoboDemo/master/docs/activity-showcase-animation-1-small.png "Activity showcase animation step 1")
&nbsp;
*3.*![Activity showcase animation step 2](https://raw.github.com/stephanenicolas/RoboDemo/master/docs/activity-showcase-animation-2-small.png "Activity showcase animation step 2")

From left to right : 

1. activity under show case
2. activity showcase, first step of animation
3. activity showcase, second & final step of animation

Overview
--------

There are some cases where application needs to provide more complex interactions that usuals, or new ones, not covered by Android UI Guidelines.

RoboDemo eases creating showcases / explaining / demonstrating of such activities to users.
It will display an overlay activity to illustrate the `Activity` under showcase. The explanations consist of a serie of
points to click on and a text. The  `Activity` under showcase is dimmed and the showcase highlights some transparent area where
the user has to click.

Creation of `DemoActivity` is straightforward, have a look at the sample to put in place RoboDemo in your own app :

1. create a `DemoActivity`, using a custom `DrawAdapter`
2. transmit widget positions and texts to display during showcase to the `DemoActivity` from the `Activity` under showcase.

RoboDemo will ease all those steps and you will just have a few lines of code to add to your project.

To learn more, visit [RoboDemo Starter Guide](https://github.com/stephanenicolas/RoboDemo/issues/1) and [browse RoboDemo Javadocs online](http://stephanenicolas.github.com/RoboDemo/apidocs/index.html).

Customization 
-------------

RoboDemo can be customized in quite a number of ways : 
* using a custom drawable
* using transparency or not to highlight the areas to click one
* using custom `Paint` to render text, and the grey area under texts
* using custom drawable and text locations
* and some more for sure...

To learn more, visit [RoboDemo Starter Guide](https://github.com/stephanenicolas/RoboDemo/issues/1) and [browse RoboDemo Javadocs online](http://stephanenicolas.github.com/RoboDemo/apidocs/index.html).


Know limitations
----------------

The base class for DemoActivity is based on `android.app.Activity`. Unfortunately, this can't cover all inheritance cases for projects
base on ActionBarSherlock or RoboGuice or a custom Activity base class per project.

In that case, we recommend using all classes from the library as well but rewrite your own `DemoActivity` changing only its super class.

In the case you use ActionBarSherlock, check the code comments, they will give you hints to support ActionBarSherlock themes.

Modules 
-------

* a library that can be used inside Eclipse (with or without m2e)
* a sample application

Configuration
-------------

* In maven : just deploy the library in your nexus, and copy sample configuration
* In Eclipse :
  * Without m2e : unzip the library in the same workspace as your project, and use the library as an android library
  * With m2e : unzip the library in the same workspace as your project, and copy dependencies in the pom of the sample 
  
Copyright and Licensing
-----------------------

Copyright Stéphane Nicolas © 2012. All rights reserved.

This library is disributed under an Apache 2.0 License.

History
-------

RoboDemo has been initiated during a project for [Octo Technology](http://www.octo.com) as we implemented a "move file" activity.
This kind of interaction is finally unusual in android and almost every file explorer app uses its custom workflow to achive it.

We decided to implement some kind of tutorial to explain our idea (basically, inspired from OI File Manager).

We asked a question about it [on Stack Over Flow](http://stackoverflow.com/q/12148381/693752) and were redirected to 
[Espiandev / ShowcaseView](https://github.com/Espiandev/ShowcaseView). We finally decided to use our own solution, and it became a 
lib in September 2012 : RoboDemo.

Alternative
------------

You can also visit [Espiandev / ShowcaseView](https://github.com/Espiandev/ShowcaseView). This project is mature and clean as well.
It provides a "native" feeling of tutorial for apps.


