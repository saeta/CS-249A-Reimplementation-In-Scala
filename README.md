# CS 249A Re-implementation in Scala #
 - Author: saeta@cs.stanford.edu
 - Project started: December 2011

# Overview #
This is a project to re-implement a frustrating assignment in C++ for Professor
Cheriton's class (CS249A at Stanford).

Tickets, and other tracking information available on [PivotalTracker][tracker].
I've been playing around and am using that for the time being. Unfortunately,
while it has better features than the GitHub tracker, it doesn't have as nice
integration. So my quest for the ideal project management system continues...

This code is, unfortunately, not my best. Please don't judge me! This is my
first serious foray into Scala, and I'm re-implementing just for fun.

Inspiration for the actors and concurrency parts of this have been taken from
[Programming In Scala, 1st Edition by Martin Odersky][pis1ed].

# Background #
In Fall 2011, I took [CS249A][cs249a]. The second two assignments (which I
have named assignments 1 and 2) were very tiresome and frustrating to write.
One of the reasons they were so frustrating is because they were implemented
in C++, in a special *Cheriton* style. In sum, the two assignments took about
8,000 lines of C++ to fully implement. I found myself very frustrated with the
methodology used in the class.

The class raised very important points vis-a-vis software engineering. I
considered issues I hadn't taken much thought before. Professor Cheriton is
absolutely right in bringing these issues up, and trying to come up with a
workable methodology. I simply don't think he has the right solution, yet.
After a productive 45 minute chat on the last day of class, I decided to
re-implement the assignments in Scala using Actors, to see if that yielded
a more satisfactory software engineering experience. This is the fruit of that
labor.

The assignment is sourced from [the CS249A website][cs249a] I have copied it
down into the assignment directory so that the assignment details last beyond
the quarter in which I took CS249A. (Note: the web pages were available to the
public, and were not behind Web-Auth, so I feel justified storing them in this
repository. Accessed: 1:10am 12/25/11)

## Personal Goals ##
I wanted to work on this project both to better develop my thoughts and
opinions on Cheriton's software engineering methodology, and to learn Scala,
play with Scala tools, play with Github, (learn Markdown,) and save myself from
utter and complete boredom during Winter Break.

## Caveats ##
In translating this assignment from C++ to Scala, there have been a few
changes. Because I'm not interfacing with a rep-layer as in the assignment,
that has been removed. Further, "introducing exception based handling" has been
removed, because it is also non-sensical, as I used exception-based handling
already (in minimal fashion).

Further, because I worked with 2 partners, we had to implement an extra
extension. (Normally group sizes are 2 people.) The extension we added is a
visualization component, where we could output the shipping network to a
graphviz file, which could then be transformed into pretty pictures.

# Design Overview: A view from 10,000 feet #
The code is split up into a few packages. The most important one is probably
the **entity** package, which specifies the main components of the simulation.
This is a shipping simulation, and thus the main entities are the Locations,
the Segments, which connect the locations, the Fleet, which holds meta-data
related to the network, Shipments, the things that traverse around the network.

The **adt** package defines a few important abstract data types, such as
distances, costs, speeds, and times. These define operators and implicit
conversions from integers. The **query** package contains helper classes for
querying the shipping network (such as calculating connections between two
locations). **sim** contains helper classes related to the simulation.

## Simulation Overview ##
The `clock` is the centerpiece of the actors-based simulation. For background
on actors-based simulations, I recommend reading the Actors and Concurrency
chapter in [Programming In Scala][pis1ed]

There are a few important invariants we maintain.

1. No new event suddenly occurs at the current time. Every event must occur
   at least one time-step in the future.
1. Actors do not send messages to one-another directly (mostly). This is
   important because we need to prevent actors from running ahead of each
   other. We leverage the above point, and channel all communication through
   the clock. The clock keeps an agenda for each time-step. That way, when
   all non-clock actors receive a `Ping` from the clock, they can immediately
   respond with a `Pong`, knowing they have received all messages for the time.

# Better and Worse #
Each of the different implementations have different strengths and weaknesses.
For example, in the Scala version, I fleshed out the abstract data types
(such as Length, Time, Cost) in Scala, whereas they were not very fleshed out
in the C++ implementation. The Scala version, however, does not have the
string-based API required of the C++ implementation.

## Changing the Model: Shipment Schedules ##
While implementing the code required for complex shipping schedules, I realized
the model used in the CS249A implementation is completely broken. There's no
reason why the shipping schedules need to be implemented inside the Locations.
Instead, they can be extracted, and be first-class citizens. Better yet, this
implementation is _much_ more flexible in terms of scheduling shipments.
In order to define a shipment schedule, all one must do is inherit from a class
that takes care of all the plumbing, etc. This is a very flexible, clean
abstraction. Now, one Location can have more than one shipping destination.

(Side note:) Because the code base is so small, it took be less than 120
seconds to change the whole implementation from having Locations be actors to
using the new ShipmentSchedule abstraction.

# Performance #
To be evaluated.

# Notes from front lines #
One of the points of the the course (CS249A) was to impress upon the
scalability and maintainability of a codebase. I think the course failed in
that regards, simply due to the huge amount of code required to write a
relatively simple simulation (4000 for assignment 1, 8000 for assignment 2).
I'm not convinced the Scala solution is much more extensible, but perhaps it
scales better simply due to the fact that it's a fraction of the size.

### Adding GraphViz support ###
Admittedly, I found adding, and testing support for graphviz to be a snap. When
I implemented in C++, I wrote a ton of different tests, broke things out to try
and make things more testable, and was fairly frustrated for a few hours on how
to support it. Perhaps because I had that experience, or perhaps also due to
the features of the language, writing the GraphViz support was a snap. The
longest time I spent was debugging the test. (Ended up being a typo in spelling
out the comment on the first line. :-D) The rich Scala APIs allowed be to
quickly write some debugging code, and I quickly figured out the problem.

In comparison, the C++ implementation required changing the whole object
hierarchy to support the double-dispatch callback mechanism. I really dislike
that approach because it requires tight coupling in the object hierarchy. It's
a cute solution, but it's unfortunately not the most sustainable, ideal
implementation of handling these sorts of application requirements.

Note: for those who are reading this and haven't taken CS249A, Double dispatch
is Professor Cheriton's method for implementing methods that would normally
have a verb as their name. Professor Cheriton advocates an attribute-only
interface. A method, such as `printSelfToStdOut()` would instead be a functor
and the receiving object above would also be a functor, whose `apply` method
would simply call the functor passing in itself.

# Conclusions #
Having rewritten almost everything, I am very pleasantly surprised at how easy
it was to:

 - Pick up Scala ([Programming In Scala][pis1ed] is a huge leg up. I'm glad it
   has been made available publicly.)
 - Set up the tools (thanks to Typesafe's freely available stack)
 - Write and refactor Scala thanks to the Scala-IDE

The Scala implementation took a fraction of the man hours, and I think is
much more simple than the C++ implementation. The Scala implementation comes in
around 400 lines of code (excluding tests; 200 more lines for tests), a factor
of 20 less. While Scala is a much richer language with more complex features, I
think Scala is a huge win in terms of maintainability, flexibility, and 
developer productivity.

## Future work ##
There are a few things to add to this project:

 - A real-time scheduler.
 - More statistics on shipments.
 - Segment capacity
 - More fleshed out ADTs for capacity, and shipment sizes. 

One other future thought is to combine the clock and fleet. They're really
one in the same, separated only by the fact that they have slightly different
concerns. The fleet is holding metadata about the network, while the clock is
dealing with the semantics of the simulation.

[pis1ed]: http://www.artima.com/pins1ed/ "Programming In Scala, 1st Edition"
[cs249a]: http://cs249a.stanford.edu/ "CS 249A Website"
[tracker]: https://www.pivotaltracker.com/projects/441541 "CS249A in Scala - Tracker"
