# Hello world! #
Author: saeta@cs.stanford.edu

This is a project to reimplement a frustrating assignment in C++ for Professor
Cheriton's class (CS249A at Stanford).

The assignment is sourced from http://cs249a.stanford.edu/ I have copied it
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
Programming In Scala, 1st Edition by Martin Odersky available on the web at:
(http://www.artima.com/pins1ed/)

### Notes from front lines ###
One of the points of the the course (CS249A) was to impress upon the
scalability and maintainability of a codebase. I think the course failed in
that regards, simply due to the huge amount of code required to write a
relatively simple simulation (4000 for assignment 1, 8000 for assignment 2).
I'm not convinced the Scala solution is much more extensible, but perhaps it
scales better simply due to the fact that it's a fraction of the size.

##### TODO #####
Where am I? I have most of the basic machinery for the simulation. Problems
that need to be addressed: creating the shipments, stats, tests! Anything else?
Who knows... But right now, it's time for bed.

### Better and Worse ###
Each of the different implementations have different strengths and weaknesses.
For example, in the Scala version, I fleshed out the abstract data types
(such as Length, Time, Cost) in Scala, whereas they were not very fleshed out
in the C++ implementation. The Scala version, however, does not have the
string-based API required of the C++ implementation.
