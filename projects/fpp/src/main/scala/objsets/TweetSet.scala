package objsets

class Tweet(val user: String, val text: String, val retweets: Int) {
  override def toString: String =
    "User: " + user + "\n" +
    "Text: " + text + " [" + retweets + "]"
}

abstract class TweetSet {

  // This method takes a predicate and returns a subset of all the elements
  // in the original set for which the predicate is true.
  def filter(predicate: Tweet => Boolean): TweetSet =
    filter0(predicate, new Empty)

  def filter0(predicate: Tweet => Boolean, accumulator: TweetSet): TweetSet

  def union(that: TweetSet): TweetSet

  // Hint: the method "remove" on TweetSet will be very useful.
  def ascendingByRetweet: Trending = {
    def collectAscendingRetweets(accumulator: Trending, tweets: TweetSet): Trending =
      if (tweets.isEmpty) accumulator
      else {
        val minimum = tweets.findMin
        collectAscendingRetweets(accumulator + minimum, tweets.remove(minimum))
      }
    collectAscendingRetweets(new EmptyTrending, this)
  }

  // The following methods are provided for you, and do not have to be changed
  def incl(tweet: Tweet): TweetSet
  def contains(tweet: Tweet): Boolean
  def isEmpty: Boolean
  def head: Tweet
  def tail: TweetSet

  // This method takes a function and applies it to every element in the set.
  def foreach(f: Tweet => Unit) {
    if (!isEmpty) {
      f(head)
      tail.foreach(f)
    }
  }

  def remove(tw: Tweet): TweetSet

  def findMin0(currentMinimum: Tweet): Tweet = {
    def minimum(tweet1: Tweet, tweet2: Tweet): Tweet =
      if (tweet2.retweets < tweet1.retweets) tweet2 else tweet1
    if (isEmpty) currentMinimum
    else tail.findMin0(minimum(currentMinimum, head))
  }

  def findMin: Tweet =
    this.tail.findMin0(this.head)
}

class Empty extends TweetSet {
  def filter0(p: Tweet => Boolean, accumulator: TweetSet): TweetSet =
    accumulator

  def union(that: TweetSet) =
    that

  def contains(x: Tweet): Boolean =
    false

  def incl(x: Tweet): TweetSet =
    new NonEmpty(x, new Empty, new Empty)

  def isEmpty =
    true

  def head =
    throw new Exception("Empty.head")

  def tail =
    throw new Exception("Empty.tail")

  def remove(tw: Tweet): TweetSet =
    this
}

class NonEmpty(elem: Tweet, left: TweetSet, right: TweetSet) extends TweetSet {

  def filter0(p: Tweet => Boolean, accumulator: TweetSet): TweetSet =
    right.filter0(p, left.filter0(p, if (p(elem)) accumulator.incl(elem) else accumulator))

  def union(that: TweetSet): TweetSet = {
    def collect(target: TweetSet, source: TweetSet): TweetSet =
      if (source.isEmpty) target
      else collect(target.incl(source.head), source.tail)

    collect(this, that)
  }

  // The following methods are provided for you, and do not have to be changed
  def contains(x: Tweet): Boolean =
    if (x.text < elem.text) left.contains(x)
    else if (elem.text < x.text) right.contains(x)
    else true

  def incl(x: Tweet): TweetSet =
    if (x.text < elem.text) new NonEmpty(elem, left.incl(x), right)
    else if (elem.text < x.text) new NonEmpty(elem, left, right.incl(x))
    else this

  def isEmpty =
    false

  def head =
    if (left.isEmpty) elem
    else left.head

  def tail =
    if (left.isEmpty) right
    else new NonEmpty(elem, left.tail, right)

  def remove(tweet: Tweet): TweetSet =
    if (tweet.text < elem.text) new NonEmpty(elem, left.remove(tweet), right)
    else if (elem.text < tweet.text) new NonEmpty(elem, left, right.remove(tweet))
    else left.union(right)
}


/** This class provides a linear sequence of tweets. */
abstract class Trending {
  def + (tw: Tweet): Trending
  def head: Tweet
  def tail: Trending
  def isEmpty: Boolean
  def foreach(f: Tweet => Unit) {
    if (!this.isEmpty) {
      f(this.head)
      this.tail.foreach(f)
    }
  }
}

class EmptyTrending extends Trending {
  def + (tweet: Tweet) =
    new NonEmptyTrending(tweet, new EmptyTrending)
  def head: Tweet =
    throw new Exception
  def tail: Trending =
    throw new Exception
  def isEmpty: Boolean =
    true
  override def toString =
    "EmptyTrending"
}

class NonEmptyTrending(elem: Tweet, next: Trending) extends Trending {
  /** Appends tw to the end of this sequence. */
  def + (that: Tweet): Trending =
    new NonEmptyTrending(elem, next + that)
  def head: Tweet =
    elem
  def tail: Trending =
    next
  def isEmpty: Boolean =
    false
  override def toString =
    "NonEmptyTrending(" + elem.retweets + ", " + next + ")"
}

object GoogleVsApple {

  def toTweetSet(keywords: List[String]): TweetSet = {

    def contains(text: String, keywords: List[String]): Boolean =
      if (keywords.isEmpty) false
      else if (text.contains(keywords.head)) true
      else contains(text, keywords.tail)

    def updated(target: TweetSet, source: Tweet) =
      if (contains(source.text, keywords)) target.incl(source)
      else target

    def collect(target: TweetSet, source: TweetSet): TweetSet =
      if (source.isEmpty) target
      else collect(updated(target, source.head), source.tail)

    collect(new Empty, TweetReader.allTweets)
  }

  val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
  val apple  = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

  val googleTweets: TweetSet = toTweetSet(google)
  val appleTweets:  TweetSet = toTweetSet(apple)

  // Q: from both sets, what is the tweet with highest #retweets?
  val trending: Trending = (googleTweets.union(appleTweets).ascendingByRetweet)
}

object Main extends App {
  println("RANKED:")
  GoogleVsApple.trending foreach println
}
