// SYNTAX DOCUMENATION

Here is the syntax, with this you should be able to do quite a bit....

* literals
Number
	Example: 1234.4
String
	Example: "abc\"def"

* built in objects
true
false
nil
undefined

* built in functions
clone(<parent>)
Example: clone(Object)
this (returns the current context in a method)
Example: this
print( <values> )
println( <values> )
strf( formatstring, List( <args> ) )
List( <values> )
if ( <cond> <true> <false> )
try( <cond> <finally> )
loop( <list> <func> )
while( <cond> <block> )

* create a variable in the current scope
var <name>
Example: var foo

* assignment
<name> = <value>
Example: foo = bar.baz("abc")

* slot access
<object>.<slot>
Example: foo.bar

* create a new Object
Example: new(Object)

* define a function (can be invoked)
[ <params> ] { <code> }
Example: [a b] { a * b + a }

* define a block (can be invoked)
{ <code> }
Example: { println( "hi" ) }

* invoke a function
<obj> ( <args> )
Example: foo( "abc", bar() )

* math operations
+ - * / % ^
Example: 1 + 2 * 4 / 4 % 6

* comparisons
< <= == != > >=
Example: 12 < 14
== != work for booleans as well
Example: true != false

* logical operations

&& ||
Example true && false || true
