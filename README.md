# COMP250-A3-Tester

A collaborative test suite for the third assignment of COMP 250 at McGill University (Winter 2021). It includes all the tests from the instructor minitester as well as extra tests written by students.

## Important

Only add TEST CODE here, not actual assignment code. If you accidentally pushed your assignment code, please delete it right away.

## How To Use

- [Recommended] You may clone this repo to your Intellij or your preferred IDE. If you aren't familiar with Git, our amazing TA Sasha made a tutorial: https://www.youtube.com/playlist?list=PLFvevpoGcNCvjyTjOfPhzqjgb-L_WdX8r
- If you aren't familiar with Git, you can download the files from the main repo page by clicking ↓Code > Download ZIP. You must put the source code in the default package with the other assignment files.
- [Not recommended] Worst case scenario, you may manually copy paste all files into the default package.
- You may also add new tests and create pull requests.
- Happy coding and debugging!

## Installing

#### For IntelliJ IDEA:

VCS -> Get from Version Control... -> Paste the URL of this repository

#### For Eclipse:

File -> Import -> Git -> Projects from Git (With Smart Import) -> Clone URI -> paste URL of this repository into the URI box -> Click next a bunch, setting directory at your own discression, Master branch from origin. All else default -> Finish

#### For both:

Drag your .java files from the assignment into the default package.

Regularly update the tester (pull the repository). Your assignment files will be ignored.

## Using the DecisionTreeVisualizer

At the top of every FillDTNode test in Tester.java you'll find two booleans: verbose and force (which should both be false by default). If verbose is set to true and that test fails, the DecisionTreeVisualizer will be launched and the main tester will pause. If both verbose and force are set to true, the visualizer will be launched even if the test passes.

When the visualizer launches, two windows will be created. The one titled "DecisionTree Visualizer: Expected" shows the DecisionTree that should have been created. The one titled "DecisionTree Visualizer: Received" shows the DecisionTree that was produced by your code.

By default, the tree is trimmed to a certain maximum height. If you wish to see the entire tree, you can open DecisionTreeVisualizer.java, comment out line 30, and uncomment line 31. Note that this will make the tree hard to read in many cases.
