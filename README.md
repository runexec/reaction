# Reaction

A small reactive programming library for Clojure

## Usage


```clojure
user> (use '[reaction.core])
nil
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
user> (defn reactive-minus-2 []
	(- @my-int 2))
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

## License

Copyright © 2013 runexec

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
