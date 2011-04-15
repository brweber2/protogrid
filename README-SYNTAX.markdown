# SYNTAX DOCUMENATION

## Here is the syntax, with this you should be able to do quite a bit....

### literals
* Number

>    1234.4

* String

>    "abc\\\"def"

### built in objects
> true

> false

> nil

> undefined

### built in functions
* clone(<parent>)

>    clone(Object)

* this (returns the current context in a method)

>    this

* print( <values> )
* println( <values> )
* strf( formatstring, List( <args> ) )
* List( <values> )
* if ( <cond> <true> <false> )
* try( <cond> <finally> )
* loop( <list> <func> )
* while( <cond> <block> )

### create a variable in the current scope
* var _name_

>    var foo

### assignment
* _name_ = _value_

>    foo = bar.baz("abc")

### slot access
* _object_._slot_

> foo.bar

### create a new Object
> clone(Object)

### define a function (can be invoked)
* [ _params>_] { _code_ }

> [a b] { a * b + a }

### define a block (can be invoked)
* { _code>_}

> { println( "hi" ) }

### invoke a function
* _obj_ ( _args_ )

> foo( "abc", bar() )

### math operations
* + - * / % ^

> 1 + 2 * 4 / 4 % 6

### comparisons
* < <= == != > >=

> 12 < 14

* == != work for booleans as well

> true != false

### logical operations
* && ||

> true && false || true

### not sure about something, view the ast
* ast( expression )

> ast( 2 + 3 * 5 )

