package bloxorz

class Move

case object Left  extends Move
case object Right extends Move
case object Up    extends Move
case object Down  extends Move

case class Position(x: Int, y: Int) {
  def +(dx: Int, dy: Int) = copy(x + dx, y + dy)
}

object Block {
  def apply(position: Position): Block = Block(position, position)
  def apply(x: Int, y: Int): Block = Block(Position(x, y))
  def apply(x1: Int, y1: Int, x2: Int, y2: Int): Block = Block(Position(x1, y1), Position(x2, y2))
}

case class Block(position1: Position, position2: Position) {
  require(standing || vertical || horizontal)

  def standing:   Boolean = position1 == position2
  def vertical:   Boolean = position1.x == position2.x && position2.y - position1.y == 1
  def horizontal: Boolean = position1.y == position2.y && position2.x - position1.x == 1

  def +(move: Move): Block = {
    if (standing)
      move match {
        case Left  => Block(position1 +(-2,  0), position1 +(-1,  0))
        case Right => Block(position1 +( 1,  0), position1 +( 2,  0))
        case Up    => Block(position1 +( 0,  1), position1 +( 0,  2))
        case Down  => Block(position1 +( 0, -2), position1 +( 0, -1))
      }
    else if (horizontal)
      move match {
        case Left  => Block(position1 +(-1,  0))
        case Right => Block(position2 +( 1,  0))
        case Up    => Block(position1 +( 0,  1), position2 +(0,  1))
        case Down  => Block(position1 +( 0, -1), position2 +(0, -1))
      }
    else
      move match {
        case Left  => Block(position1 +(-1,  0), position2 +(-1, 0))
        case Right => Block(position1 +( 1,  0), position2 +( 1, 0))
        case Up    => Block(position2 +( 0,  1))
        case Down  => Block(position1 +( 0, -1))
      }
  }

  def neighbors: List[(Block, Move)] =
    List((this + Left, Left), (this + Right, Right), (this + Up, Up), (this + Down, Down))
}

object Terrain {
  def map(terrain: Array[String]): IndexedSeq[(Position, Char)] =
    for {
      y <- 0 until terrain.size
      x <- 0 until terrain(y).size
      char = terrain(y).charAt(x)
      if char != '-'
    } yield {
      (Position(x, terrain.size - y - 1), char)
    }

  def apply(terrain: String) =
    new Terrain(map(terrain.split("\n")).toMap.withDefaultValue('-'))
}

class Terrain(val map: Map[Position, Char]) {
  val start  = find('S')
  val target = find('T')

  def find(character: Char): Position =
    map.find(_._2 == character).get._1

  def holds(block: Block): Boolean =
    map.contains(block.position1) && map.contains(block.position2)

  override def toString: String = {
    val maximum: Position = Position(map.keys.maxBy(_.x).x, map.keys.maxBy(_.y).y)

    def line(y: Int) = for { x <- 0 to maximum.x } yield { map.get(Position(x, y)).getOrElse('-') }

    def lines = for { y <- maximum.y to 0 by -1 } yield { line(y).mkString }

    lines.mkString("\n")
  }
}

case class Path(block: Block, moves: List[Move])

case class Solver(terrain: Terrain) {
  def from(paths: Stream[Path], explored: Set[Block]): Stream[Path] =
    if (paths.isEmpty) Stream.empty
    else {
      val candidates = for {
        path <- paths
        (neighbor, move) <- path.block.neighbors
        if !explored.contains(neighbor)
        if terrain.holds(neighbor)
      } yield {
        Path(neighbor, move :: path.moves)
      }
      paths #::: from(candidates, explored ++ (candidates.map(_.block)))
    }

  val start = Path(Block(terrain.start), List())
  val goal  = Block(terrain.target)

  lazy val paths: Stream[Path] = from(Stream(start), Set())

  lazy val pathsToGoal: Stream[Path] =
    for {
      path: Path <- paths
      if path.block == goal
    } yield {
      path
    }
}
