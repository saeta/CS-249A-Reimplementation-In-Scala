# Hello world! #
Author: saeta@cs.stanford.edu

This is a project to re-implement a frustrating assignment in C++ for Professor
Cheriton's class (CS249A at Stanford).

The assignment is sourced from [the CS249A website][cs249a] I have copied it
down into the assignment directory so that the assignment details last beyond
the quarter in which I took CS249A. (Note: the web pages were available to the
public, and were not behind Web-Auth, so I feel justified storing them in this
repository. Accessed: 1:10am 12/25/11)

In translating this assignment from C++ to Scala, there have been a few
changes. Because I'm not interfacing with a rep-layer as in the assignment,
that has been removed. Further, "introducing exception based handling" has been
removed, because it is also non-sensical, as I used exception-based handling
already (in minimal fashion).

This code is, unfortunately, not my best. Please don't judge me!

Inspiration for the actors and concurrency parts of this have been taken from
[Programming In Scala, 1st Edition by Martin Odersky][pis1ed].

## Better and Worse ##
Each of the different implementations have different strengths and weaknesses.
For example, in the Scala version, I fleshed out the abstract data types
(such as Length, Time, Cost) in Scala, whereas they were not very fleshed out
in the C++ implementation. The Scala version, however, does not have the
string-based API required of the C++ implementation.

### Changing the Model: Shipment Schedules ###
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

## Performance ##
To be evaluated.

## Notes from front lines ##
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
out the comment on the first line. :-D)


[pis1ed]: http://www.artima.com/pins1ed/ "Programming In Scala, 1st Edition"
[cs249a]: http://cs249a.stanford.edu/ "CS 249A Website"