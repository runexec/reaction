# Reaction

A small reactive programming library for Clojure

[API Docs](http://runexec.github.io/soft-docs/reaction/reaction.core.html)

## Usage


```clojure
user> (use '[reaction.core])
nil
reaction.core> (def-reactive! my-int 1)
#'reaction.core/my-int
reaction.core> (push! my-int inc inc)
nil
reaction.core> @my-int
3
reaction.core> (pop! my-int)
nil
reaction.core> @my-int
2
reaction.core> (pop! my-int)
nil
reaction.core> @my-int
1
reaction.core> (push! my-int inc (partial + 5) dec)
nil
reaction.core> @my-int
6
reaction.core> (pop! my-int 3)
nil
reaction.core> @my-int
1
user> (def-reactive! my-int 123)
#'user/my-int
user> @my-int
123
user> (rapply! my-int inc)
nil
user> @my-int
124
user> (original-value my-int)
123
user> @my-int
124
user> (rapply! my-int #(+ 5 %))
nil
user> @my-int
129
user> (defn reactive-minus-2 [] (- @my-int 2))
#'user/reactive-minus-2
user> (reactive-minus-2)
127
user> (rapply! my-int #(- % 20))
nil
user> @my-int
109
user> (reactive-minus-2)
107
user> (rset! my-int 1)
nil
user> (remove-actions! my-int)
nil
user> @my-int
1
user> (rapply! my-int inc)
nil
user> @my-int
2
user> (remove-actions! my-int)
nil
user> @my-int
1
user> 
```

## Changes

##### 0.1.1

Added '''push!''' and '''pop!'''
Fixed Active '''deftype'''

## License

Copyright © 2013 runexec

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
