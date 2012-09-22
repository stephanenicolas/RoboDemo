RoboDemo
========

RoboDemo is a ShowCase library for Android to demonstrate how a given Activity works to users.

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

RoboDemo can be customized in quite a number of ways : 
* using a custom drawable
* using transparency or not to highlight the areas to click one
* using custom `Paint` to render text, and the grey area under texts
* using custom drawable and text locations
* and some more for sure...

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
  * With m2e : unzip the library in the same workspace as your project, and copy sample configuration 
  
