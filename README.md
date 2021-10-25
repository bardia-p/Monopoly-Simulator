# Monopoly Simulator

<p align="center">
<img src="images/rich-uncle-pennybags.jpg" />
</p>

## Description
This repository simulates the popular board game monopoly. In this interactive business game you will try to outwit your
opponents by making them go bankrupt while purchasing properties
around the board. Spend wisely and aim for a TOTAL MONOPOLY. 

Please user to the user manual on the 'documents' folder 
to get familiar with the different commands in the game.

## Deliverables
<ol>
  <li>Milestone 1: Text-based playable version of the Monopoly game</li>
  <li>Milestone 2: GUI-based version of the Monopoly game</li>
  <li>Milestone 3: Additional Monopoly features and addition of AI</li>
  <li>Milestone 4: Save/load features and international versions</li>
</ol>

## Authors
Group 3\
Bardia Parmoun - 101143006\
Kyra Lothrop - 101145872\
Sarah Chow - 101143033\
Owen VanDusen - 101152022 

## Changes Since Previous Deliverable
<p>Currently working on the first milestone - Milestone 1. No previous deliverables. 
Current version of the game implements the basic functionality of the monopoly with options for buying/selling 
properties while implementing features for paying rent and tax on special cells. The current milestone also has option 
for the different types of cell such as Go, Property, Tax, Jail, and Free Parking. These properties will later be
implemented in milestones 2 and 3.</p>

## Known Issues
<ul>
  <li>
    Although the current milestone takes advantage of the MVC design, there are instances where the controller class 
    has print statements in order to be able to offer the different commands that a user can use. This is in conflict 
    with the MVC pattern since the controller should not do anything related to outputs; however, since the current 
    version of the UI is text based, this group decided to fix this issue in the next milestone where those print
    statements will be replaced with prompts. Implementing that feature in this milestone would have resolved in a lot
    of temporary methods.
  </li> 
  <li>
    Another source of issue could the string manipulation that is used in this milestone. This user interface relies on
    a lot of string manipulation by using methods such as a contains. Although, there have been many checks implemented 
    to avoid problems like that, these could result in some issues. This will be resolved in the future milestones where
    the user interface will no longer be text based.
  </li>
</ul>

## Roadmap Ahead
<p>Complete Milestone 2: GUI-based version of the Monopoly game:

In the future milestone the current functionality of the game will be replaced with a graphical user interface. After 
that the other functionalities of the game such as other special properties will be implemented, and finally the game 
will have the option to be customizable. 
</p>
