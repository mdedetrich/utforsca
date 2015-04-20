# utforsca: Utils for scala

[utforsca][1] is a collection of utils that I have personally deemed incredibly useful for scala. The project follows these principles
in regards to what gets added

* Small: Utils that are added are small, [utforsca][1] isn't designed to be a kitchen sink library that implements every single use case. Utils
    are only added if they are deemed as necessary (often because they are not possible in Scala )
* Clean and Simple: The Utils that are added are designed to be very simple, clean and easy to use. In other words, the utils are designed to leverage
    scala as much as possible. [utforsca][1] is not designed to be [scalaz][2], the utils being added aren't meant to be as generic as some other
    libraries (possibly being restrictive on purpose), they are designed to be easy to pick up and use
* Easy to maintain: Because of the the previous two points, [utforsca][1] is designed to be easy/trivial to maintain. API changes occurrences
    are to happen as minimal as possible (ideally never)

# Installation

First make sure you have the `mdedetrich-releases` repository in your `build.sbt` or your `project/<somefile>.scala`.

    resolvers ++= Seq("mdedetrich-releases" at "http://artifactory.mdedetrich.com/libs-release-local")

Then simply add the following dependency to `libraryDependencies`, for Scala `2.11.x`

    "com.mdedetrich" %% "utforsca" % "2.1.0"

Or for `Scala 2.10.x`

    "com.mdedetrich" %% "utforsca" % "1.1.0"
        
# Contents

Here is a list of the utils contained within [utforsca][1]

## SealedContents

```scala
import com.mdedetrich.utforsca.SealedContents
```

SealedContents is an incredibly handy macro taken from [here](http://stackoverflow.com/questions/13671734/iteration-over-a-sealed-trait-in-scala).
The macro automatically constructs a `Set[T]` of an [ADT](http://en.wikipedia.org/wiki/Abstract_data_type) `T`, that contains an enumeration of all the types
contained within `T`. As a very simple example, assume that we create some ADT `Title`, which contains various Titles that can be attributed to a person

```scala
sealed abstract class Title(val id:Long val formalName:String)
```

Lets now populate our `Title` with various entries

```scala
case object Mr extends Title(1,"Mr")
case object Mrs extends Title(2,"Mrs")
case object Miss extends Title(3,"Miss")
case object Ms extends Title(4,"Ms")
case object Dr extends Title(5,"Dr")
case object Professor extends Title(6,"Professor")
```

Now lets create a companion object which holds our sealed contents

```scala
object Title {
    val all:Set[Title] = SealedContents.values[Title]
}
```

`Title.all` will now contain an enumeration of all of the child values of Title, which lets us do stuff like this

```scala
// Find a title by id
Title.all.find(_.id == id)
// Enumerate through every title to get its id
Title.all.map(_.id)
```

Very obvious use case for this is allowing to represent enumerated types easily in a database. If you are using something
like [Slick][3], you can easily create mappers going from, and to, type `Title`. Its also a very non verbose way to define static
databases in your code. Its a very nice, type safe replacement for Java/Scala's `Enum` type

### Limitations

Limitations exist for `SealedContents`, however they are quite obvious. The type `T` that you run `SealedContents.value[T]` on, must

* Be `sealed`: The compiler needs to now all possible child values at compile time to generate enumeration
* Every child must be an `case object`, not a `case class`: `case object`s can be instantiated without a constructor, so its actually
possible to create instances of these types at runtime

## asMap

`asMap` converts a case class to a map. In other words, if you have a case class like the following

```scala
case class SomeCaseClass(string:String,int:Int)
val someCaseClass = SomeCaseClass("rawr",5)
```

`asMap` will convert the case class to a map

```scala
val map = someCaseClass.asMap
println(map)
```

Will print the following

```scala
Map(
    "string" -> "rawr",
    "rawr" -> 5
)
```

`asMap` will also work for existential types, i.e. if you are using something like Spire and do the following

```scala
case class SomeNumericCaseClass[T : Numeric](someNumeric:T,listOfSomeNumeric:List[T])
```

Check the tests for more info

### Limitations

asMap will often return a map of type `Map[String,Any]`, this means you will probably have to match against the value to check for its type.
In other words, you will probably end up doing something like this

```scala
val map = someCaseClass.asMap

map.foreach{case(key,value) =>
    value match {
        case s:String => println(s"key:$key contains a string:$s")
        case i:Int => println(s"key:$key contains an int:$i")
    }
}
```

[1]:https://github.com/mdedetrich/utforsca
[2]:https://github.com/scalaz/scalaz
[3]:http://slick.typesafe.com/